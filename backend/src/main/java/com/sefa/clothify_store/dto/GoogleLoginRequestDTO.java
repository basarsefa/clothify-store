package com.sefa.clothify_store.dto;

import jakarta.validation.constraints.NotBlank;

public record GoogleLoginRequestDTO(
        @NotBlank(message = "Token boş olamaz.")
        String accessToken) {
}
