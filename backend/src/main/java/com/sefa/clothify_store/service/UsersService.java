package com.sefa.clothify_store.service;

import com.sefa.clothify_store.dto.*;
import com.sefa.clothify_store.entity.Role;

import com.sefa.clothify_store.entity.UserPrincipal;
import com.sefa.clothify_store.entity.Users;
import com.sefa.clothify_store.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class UsersService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    public final WebClient.Builder webClientBuilder;



    public ResponseEntity<String> register(RegisterRequestDTO request) {

        if (request.password() == null || request.password().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Normal kayıt için şifre zorunludur.");
        }

        if(userRepository.existsByEmail(request.email())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bu e-posta kullanılmaktadır.");
        }
        var user = Users
                .builder()
                .name(request.displayName())
                .email(request.email())
                .password(encoder.encode(request.password()))
                .build();

        user.setRole(Role.USER);
        userRepository.save(user);

        return ResponseEntity.ok("Kayıt başarılı");
    }

    public AuthenticationResponseDTO login(AuthenticationRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        /*Yukardaki satır istekte gelen kullanıcı bilgileri ile veritabanındaki bilgileri kontrol eder
        * eğer yanlışsa hata fırlatır ve program durur. Hata fırlatmazsa aşağıdaki koddan devam eder.*/

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwtToken = jwtService.generateToken(userDetails);
        return new AuthenticationResponseDTO(jwtToken);
    }


    public AuthenticationResponseDTO loginWithGoogle( GoogleLoginRequestDTO request) {

        try {
            GoogleUserInfoDTO userInfo = webClientBuilder.build()
                    .get()
                    .uri("https://www.googleapis.com/oauth2/v3/userinfo")
                    .header("Authorization", "Bearer " + request.accessToken())
                    .retrieve()
                    .bodyToMono(GoogleUserInfoDTO.class)
                    .block();

            if (userInfo == null || userInfo.email()==null){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Google'dan kullanıcı bilgileri alınamadı");
            }

            Users user = userRepository.findByEmail(userInfo.email()).orElseGet(() -> {
                Users newUser = Users.builder()
                        .email(userInfo.email())
                        .name(userInfo.name())
                        .password(null)
                        .role(Role.USER)
                        .build();
                return userRepository.save(newUser);
            });
            UserDetails userDetails = new UserPrincipal(user);
            String jwtToken = jwtService.generateToken(userDetails);

            return new AuthenticationResponseDTO(jwtToken);
        }
        catch (ResponseStatusException e) {
            throw e;
        }catch (Exception e) {
            throw new RuntimeException("Sistemsel hata oluştu: " + e.getMessage());
        }
    }
}
