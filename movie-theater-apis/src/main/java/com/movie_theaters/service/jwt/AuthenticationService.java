package com.movie_theaters.service.jwt;

import com.movie_theaters.dto.request.LoginRequest;
import com.movie_theaters.dto.response.TokenResponse;
import com.movie_theaters.entity.User;
import com.movie_theaters.repository.UserRepository;
import com.movie_theaters.service.jwt.JwtService;
import com.movie_theaters.util.TokenType;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public TokenResponse authentication(LoginRequest loginRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        var user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new UsernameNotFoundException("tài khoản mật khẩu không chính xác!"));



        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        log.info("time {} -  username {} - roles {}", LocalDateTime.now(), user.getUsername(), roles);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .roles(roles)
                .build();
    }

    public TokenResponse refresh(HttpServletRequest request) {
        String refreshToken = request.getHeader("x-token");
        if(StringUtils.isBlank(refreshToken)){
            throw new IllegalArgumentException("Token must be not blank");
        }
        final String userName = jwtService.extractUsername(refreshToken, TokenType.REFRESH_TOKEN);
        Optional<User> user = userRepository.findByUsername(userName);
        if(!jwtService.isValidToken(refreshToken,TokenType.REFRESH_TOKEN, user.get())){
            throw new IllegalArgumentException("Token is invalid");
        }

        String accessToken = jwtService.generateToken(user.get());

        List<String> roles = user.get().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.get().getId())
                .roles(roles)
                .build();
    }
}
