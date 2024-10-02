package com.movie_theaters.dto.response;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException)
            throws IOException {

        // if (authException instanceof InsufficientAuthenticationException) {
        // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // response.setContentType("application/json");
        // response.getWriter().write("{\"status\":\"401\",\"message\":\"Unauthorized: "
        // + authException.getMessage() + "\"}");
        // } else {
        // Thực hiện hành động mặc định cho các lỗi khác
        // response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        // response.setContentType("application/json");
        // response.getWriter().write("{\"status\":\"403\",\"message\":\"Access Denied:
        // " + authException.getMessage() + "\"}");
        // }
    }
}
