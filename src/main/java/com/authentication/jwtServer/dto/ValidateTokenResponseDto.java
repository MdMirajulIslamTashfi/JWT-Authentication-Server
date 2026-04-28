package com.authentication.jwtServer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidateTokenResponseDto {
    private boolean valid;
    private String message;
    private String email;
    private String role;
}
