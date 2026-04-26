package com.authentication.jwtServer.controlller;

import com.authentication.jwtServer.dto.TokenRequestDto;
import com.authentication.jwtServer.dto.TokenResponseDto;
import com.authentication.jwtServer.service.JwtServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jwt")
@RequiredArgsConstructor
public class JwtServerController {
    private final JwtServerService jwtServerService;

    @PostMapping("/generate")
    public ResponseEntity<TokenResponseDto> generate(@RequestBody TokenRequestDto tokenRequestDto) {
        TokenResponseDto tokenResponseDto = jwtServerService.generateToken(tokenRequestDto);
        return ResponseEntity.ok(tokenResponseDto);
    }
}
