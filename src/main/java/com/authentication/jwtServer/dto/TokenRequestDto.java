package com.authentication.jwtServer.dto;

import lombok.Data;

@Data
public class TokenRequestDto {
    private String email;
    private String password;
}
