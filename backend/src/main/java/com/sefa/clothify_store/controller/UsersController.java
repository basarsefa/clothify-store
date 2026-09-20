package com.sefa.clothify_store.controller;
import com.sefa.clothify_store.dto.AuthenticationRequestDTO;
import com.sefa.clothify_store.dto.AuthenticationResponseDTO;
import com.sefa.clothify_store.dto.GoogleLoginRequestDTO;
import com.sefa.clothify_store.dto.RegisterRequestDTO;
import com.sefa.clothify_store.service.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UsersController {
    private final UsersService usersService;

    @PostMapping("/v1/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDTO request){
       return usersService.register(request);
    }

    @PostMapping("/v1/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@Valid @RequestBody AuthenticationRequestDTO request) {
        return ResponseEntity.ok(usersService.login(request));
    }

    @PostMapping("/v1/google-login")
    public ResponseEntity<AuthenticationResponseDTO> googleLogin(@Valid @RequestBody GoogleLoginRequestDTO request) {
        return ResponseEntity.ok(usersService.loginWithGoogle(request));
    }
}
