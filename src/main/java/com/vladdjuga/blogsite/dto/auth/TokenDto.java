package com.vladdjuga.blogsite.dto.auth;

public record TokenDto(String accessToken, String tokenType, long expiresIn) {
    public TokenDto(String accessToken, long expiresIn) {
        this(accessToken, "Bearer", expiresIn);
    }
}

