package com.movie_theaters.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordRequest {
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống!")
    private String oldPassword;

    @NotBlank(message = "Mật khẩu không được để trống!")
    private String newPassword;

    @NotBlank(message = "Mật khẩu không được để trống!")
    private String rePassword;
}
