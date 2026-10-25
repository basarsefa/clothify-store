package com.sefa.clothify_store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequestDTO(
        @NotBlank(message = "Token boş olamaz.")
        String token,
        @NotBlank(message = "Şifre boş olamaz.")
        @Size(min = 6, message = "Şifre en az 6 karakterden oluşmalıdır.")
        String newPassword
) {
}
