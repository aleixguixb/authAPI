package com.example.auth_service.common.dtos;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
    private String accessToken;
    private Long userId; // Añadimos este value para la response
}
