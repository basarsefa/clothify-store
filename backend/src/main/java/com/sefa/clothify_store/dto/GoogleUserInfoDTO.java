package com.sefa.clothify_store.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleUserInfoDTO(String email, String name, @JsonProperty("email_verified") boolean emailVerified) {
}
