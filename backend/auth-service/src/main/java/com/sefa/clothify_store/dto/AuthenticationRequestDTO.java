package com.sefa.clothify_store.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequestDTO(
        @Email
        @NotBlank(message = "Mail boş olamaz. ")
        String email,
        @NotBlank(message = "Şifre boş olamaz. ")
        String password
)
{ }
