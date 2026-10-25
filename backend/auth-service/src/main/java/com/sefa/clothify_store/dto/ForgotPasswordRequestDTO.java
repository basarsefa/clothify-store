package com.sefa.clothify_store.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequestDTO(
        @Email
        @NotBlank(message = "Email boş olamaz.")
        String email) {
}
