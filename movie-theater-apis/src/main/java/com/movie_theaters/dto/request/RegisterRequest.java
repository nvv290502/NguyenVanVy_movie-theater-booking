package com.movie_theaters.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "username không được để trống!")
    private String username;

    @NotBlank(message = "password không được để trống!")
    private String password;

    @NotBlank(message = "email không được để trống!")
    @Email(message = "email không đúng!")
    private String email;

    @NotBlank(message = "re-password không được để trống!")
    private String rePassword;
}
