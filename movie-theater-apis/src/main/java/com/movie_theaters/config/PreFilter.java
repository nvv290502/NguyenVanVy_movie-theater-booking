package com.movie_theaters.config;

import com.movie_theaters.exception.TokenException;
import com.movie_theaters.service.jwt.JwtService;
import com.movie_theaters.service.UserService;
import com.movie_theaters.util.TokenType;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class PreFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws  ServletException, IOException {
        log.info("--------------------------PreFilter---------");
        String path = request.getRequestURI();
        if (path.startsWith("/oauth2/")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authorization = request.getHeader("Authorization");
        log.info("Authorization: {}", authorization);

        if(StringUtils.isBlank(authorization) || !authorization.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authorization.substring("Bearer ".length());
        log.info("Token {}", token);

        try {
            final String userName = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
            log.info("username: {}", userName);

            if (StringUtils.isNotEmpty(userName) && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userName);
                if (!userDetails.isEnabled()) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"status\":\"403\",\"message\":\"Account Disable\"}");
                    return;
                }
                if (jwtService.isValidToken(token, TokenType.ACCESS_TOKEN, userDetails)) {
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    log.info("authorization {}", userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authentication);
                    SecurityContextHolder.setContext(context);
                }
            }
            filterChain.doFilter(request, response);
        } catch (TokenException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":\"401\",\"message\":\"" + ex.getMessage() + "\"}");
            log.error("TokenException: {}", ex.getMessage());
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":\"500\",\"message\":\"Có lỗi xảy ra trong quá trình xử lý\"}");
            log.error("Exception: {}", ex.getMessage());
        }
//        filterChain.doFilter(request, response);
    }
}
