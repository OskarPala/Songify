package com.songify.infrastructure.security.jwt;

import org.springframework.stereotype.Component;

@Component
class JwtTokenGenerator {
    String authenticateAndGenerateToken( String username,  String password) {
        return "token123";
    }
}
