package com.fpoly.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fpoly.repository.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private  UserRepository userRepository;
    private  RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration}")
    private int refreshExpiration;

    public Token createRefreshToken(String email) {
        Token refreshToken = new  Token.Builder()
                .user(userRepository.findByEmail(email).get()) // Optional<User>
                .token(UUID.randomUUID().toString())
                .expirationDate(LocalDateTime.now().plusDays(refreshExpiration * 1000L))
                .revoked(false)
                .expired(false)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<Token> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public Token verifyExpiration(Token refreshToken) {
        if (refreshToken.getExpirationDate().compareTo(LocalDateTime.now()) < 0) {
            refreshToken.setRevoked(true);
            refreshToken.setExpired(true);
            return refreshTokenRepository.save(refreshToken);
        }
        return refreshToken;
    }
}