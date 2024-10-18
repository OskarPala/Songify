package com.songify.infrastructure.security.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class JwtTokenController {

    private final JwtTokenGenerator tokenGenerator;

    @PostMapping("/token")
    public ResponseEntity<JwtResponseDto> authenticateAndGenerateToken(
            @RequestBody TokenRequestDto dto,
            HttpServletResponse response) {
        String token = tokenGenerator.authenticateAndGenerateToken(dto.username(), dto.password());
        Cookie cookie = new Cookie("accessToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60); //1 hour
        response.addCookie(cookie);
        return ResponseEntity.ok(
                JwtResponseDto.builder()
                        .token(token)
                        .build()
        );
    }
}
