package com.sefa.clothify_store.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponseDTO(
        String message,
        int status,
        String path,
        LocalDateTime time,
        String requestId
) {

}
