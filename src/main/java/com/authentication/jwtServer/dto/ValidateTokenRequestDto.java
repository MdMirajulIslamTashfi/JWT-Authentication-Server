package com.authentication.jwtServer.dto;

import lombok.Data;

@Data
public class ValidateTokenRequestDto {
    private String token;
}
