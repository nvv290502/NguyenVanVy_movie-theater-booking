package com.movie_theaters.service.jwt.impl;

import com.movie_theaters.exception.TokenException;
import com.movie_theaters.service.jwt.JwtService;
import com.movie_theaters.util.TokenType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.movie_theaters.util.TokenType.ACCESS_TOKEN;
import static com.movie_theaters.util.TokenType.REFRESH_TOKEN;

@Service
public class JwtServiceImpl implements JwtService {
    private final String secretKey = "f85a34ed03966dc5eb82e05baebeae6c17046216bfbd9065b93ea6607882c0bc";
    private final String refreshKey = "b3e9148f2f256d5fdb423e45a17958e220247f274e8060dda4eec2cfed555481";

    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities());
        return generateToken(claims, userDetails);
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return generateRefreshToken(new HashMap<>(), userDetails);
    }

    @Override
    public String extractUsername(String token, TokenType type) {
        return extractClaim(token, type, Claims::getSubject);
    }


    @Override
    public boolean isValidToken(String token,TokenType tokenType, UserDetails userDetails) {
        final String username = extractUsername(token, tokenType);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token, tokenType);
    }

    private boolean isTokenExpired(String token, TokenType type) {
        return extractExpiration(token, type).before(new Date());
    }

    private Date extractExpiration(String token, TokenType type) {
        return extractClaim(token,type, Claims::getExpiration);
    }

    private String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 1 day expiration
                .signWith(getKey(ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateRefreshToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 15)) // 15 days expiration
                .signWith(getKey(REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey(TokenType type) {
        byte[] keyBytes;
        if(ACCESS_TOKEN.equals(type)){
            keyBytes = Decoders.BASE64.decode(secretKey);
        }else{
            keyBytes = Decoders.BASE64.decode(refreshKey);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private <T> T extractClaim(String token,TokenType type, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token, type);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token, TokenType type) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey(type))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new TokenException("Token đã hết hạn");
        } catch (UnsupportedJwtException e) {
            throw new TokenException("Token không được hỗ trợ");
        } catch (MalformedJwtException e) {
            throw new TokenException("Token không hợp lệ");
        } catch (SignatureException e) {
            throw new TokenException("Chữ ký của token không chính xác");
        } catch (Exception e) {
            throw new TokenException("Lỗi khi xác thực token");
        }
    }
}
