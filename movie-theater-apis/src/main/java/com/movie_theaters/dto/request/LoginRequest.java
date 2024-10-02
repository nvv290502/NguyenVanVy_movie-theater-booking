package com.movie_theaters.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "username không được để trống!")
    private String username;
    @NotBlank(message = "password không được để trống!")
    private String password;
}
