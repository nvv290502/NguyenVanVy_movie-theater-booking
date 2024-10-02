package com.movie_theaters.config;

import com.movie_theaters.dto.response.CustomAccessDeniedHandler;
import com.movie_theaters.dto.response.CustomAuthenticationEntryPoint;
import com.movie_theaters.service.oauth2.CustomOAuth2UserService;
import com.movie_theaters.service.oauth2.OAuth2LoginSuccessHandler;
import com.movie_theaters.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;

    private final PreFilter preFilter;

    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    private String[] WHITE_LIST = {"/auth/**","/user/register","/user/login","/user/home","/user/info","/oauth2/**","/api/pub/**","/user/forgot-password", "/user/reset-password","/user/api/pub/**"};

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable());
        httpSecurity.authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITE_LIST).permitAll()
                        .requestMatchers("/user").hasAnyAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated())
                .oauth2Login(oath -> oath.userInfoEndpoint(end -> end.userService(oAuth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler))
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(provider())
                .addFilterBefore(preFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler));
        return httpSecurity.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return  webSecurity -> webSecurity.ignoring();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider provider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService.userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Autowired
    private CustomOAuth2UserService oAuth2UserService;

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;




}
