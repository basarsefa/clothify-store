package com.sefa.clothify_store.controller;
import com.sefa.clothify_store.dto.AuthenticationRequestDTO;
import com.sefa.clothify_store.dto.AuthenticationResponseDTO;
import com.sefa.clothify_store.dto.GoogleLoginRequestDTO;
import com.sefa.clothify_store.dto.RegisterRequestDTO;
import com.sefa.clothify_store.service.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UsersController {
    private final UsersService usersService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request){
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
}
