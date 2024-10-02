package com.movie_theaters.service.jwt;

import com.movie_theaters.util.TokenType;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface JwtService {

    String generateToken(UserDetails user);
    String generateRefreshToken(UserDetails user);

    String extractUsername(String token, TokenType type);

    boolean isValidToken(String token,TokenType type, UserDetails userDetails);
}
