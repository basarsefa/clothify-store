package com.sefa.clothify_store.service;

import com.sefa.clothify_store.dto.*;
import com.sefa.clothify_store.entity.Provider;
import com.sefa.clothify_store.entity.Role;

import com.sefa.clothify_store.entity.UserPrincipal;
import com.sefa.clothify_store.entity.Users;
import com.sefa.clothify_store.exception.DuplicateResourceException;
import com.sefa.clothify_store.exception.GoogleAuthenticationException;
import com.sefa.clothify_store.exception.MissingProviderAttributesException;
import com.sefa.clothify_store.exception.WrongAuthenticationProviderException;
import com.sefa.clothify_store.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UsersService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    public final WebClient.Builder webClientBuilder;



    public AuthenticationResponseDTO register(RegisterRequestDTO request) {

        if(userRepository.existsByEmail(request.email())){
            throw new DuplicateResourceException(request.email() + "this email already exists.");
        }
        var user = Users
                .builder()
                .name(request.displayName())
                .email(request.email())
                .password(encoder.encode(request.password()))
                .provider(Provider.LOCAL)
                .role(Role.USER)
                .build();

        userRepository.save(user);

        return new AuthenticationResponseDTO("Registration successful.");
    }

    public AuthenticationResponseDTO login(AuthenticationRequestDTO request) {

        Optional<Users> userOptional = userRepository.findByEmail(request.email());

        if(userOptional.isPresent() && userOptional.get().getProvider() == Provider.GOOGLE) {
            throw new WrongAuthenticationProviderException("Bu hesap Google ile oluşturulmuştur. Lütfen Google ile giriş yapın.");
        }

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

            if (userInfo == null ){
                throw new GoogleAuthenticationException("Google ile bağlantı kurulamadı veya geçersiz token");
            }

            if(userInfo.email()==null || userInfo.email().isBlank() || !userInfo.emailVerified()) {
                throw new MissingProviderAttributesException("Doğrulanmış E-posta adresi bulunamadı");
            }
            Optional<Users> existUser = userRepository.findByEmail(userInfo.email());
            Users user;

            if(existUser.isPresent()){
                user = existUser.get();
                if(user.getProvider()== Provider.LOCAL){
                    user.setProvider(Provider.GOOGLE);
                    user.setPassword(null);
                    userRepository.save(user);
                }
            }else{
                user = Users
                        .builder()
                        .name(userInfo.name())
                        .email(userInfo.email())
                        .password(null)
                        .provider(Provider.GOOGLE)
                        .role(Role.USER)
                        .build();
                userRepository.save(user);
            }

            UserDetails userDetails = new UserPrincipal(user);
            String jwtToken = jwtService.generateToken(userDetails);

            return new AuthenticationResponseDTO(jwtToken);
        }
        catch (GoogleAuthenticationException | MissingProviderAttributesException e) {
            throw e;
        }catch (WebClientResponseException e) {
            throw new GoogleAuthenticationException("Google token'ı geçersiz veya süresi dolmuş.");
        }catch (Exception e) {
            throw new RuntimeException("Sistemsel bir hata oluştu. ");
        }
    }
}
