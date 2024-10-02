package com.movie_theaters.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse implements Serializable {

    private String accessToken;
    private String refreshToken;
    private Long userId;
    private List<String> roles;

}
