package com.sefa.clothify_store.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @NotBlank(message = "Ad boş olamaz.")
        String displayName,

        @Email
        @NotBlank(message = "Mail boş olamaz.")
        String email,
        @NotBlank(message = "Şifre boş olamaz.")
        @Size(min = 6, message = "Şifre en az 6 karakterden oluşmalıdır.")
        String password
)
{ }
