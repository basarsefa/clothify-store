package com.sefa.clothify_store.controller;
import com.sefa.clothify_store.dto.*;
import com.sefa.clothify_store.entity.UserPrincipal;
import com.sefa.clothify_store.service.MyUserDetailsService;
import com.sefa.clothify_store.service.UsersService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UsersController {
    private final UsersService usersService;
   /* @GetMapping("/validate")
    public ResponseEntity<Void> validateToken(HttpServletResponse response) {
        // Gelen istek Nginx'ten geçti ve JWTFilter token'ı başarıyla valide ettiyse
        // SecurityContextHolder dolu olacaktır.
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal().toString())) {

            // İsteğe bağlı: Category veya Product servisinin kullanıcının kim olduğunu bilmesi için
            // Nginx üzerinden taşınmak üzere bir Header set edebilirsin (Örn: Email veya User ID)
            if (authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
                response.setHeader("X-User-Email", userPrincipal.getUsername());
            }

            // Token geçerli! Nginx'e 200 OK fırlatıyoruz, Nginx de geçişe izin verecek.
            return ResponseEntity.ok().build();
        }

        // Güvenlik filtresinden bir şekilde sızdıysa bile context boşsa geçirme
        return ResponseEntity.status(401).build();
    }*/

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request){
       return ResponseEntity
               .status(HttpStatus.CREATED)
               .body(usersService.register(request));

    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@Valid @RequestBody AuthenticationRequestDTO request) {
        return ResponseEntity.ok(usersService.login(request));
    }

    @PostMapping("/google-login")
    public ResponseEntity<AuthenticationResponseDTO> googleLogin(@Valid @RequestBody GoogleLoginRequestDTO request) {
        return ResponseEntity.ok(usersService.loginWithGoogle(request));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponseDTO> verifyEmail(@RequestParam("token") String token) {
        return ResponseEntity.ok(usersService.verifyEmail(token));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponseDTO> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO request) {
        return ResponseEntity.ok(usersService.forgotPassword(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponseDTO> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO request){
        return ResponseEntity.ok(usersService.resetPassword(request));
    }
}
