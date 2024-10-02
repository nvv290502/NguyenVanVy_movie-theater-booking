package com.movie_theaters.dto;

import com.movie_theaters.entity.enums.MembershipLevel;
import com.movie_theaters.entity.Role;
import com.movie_theaters.entity.enums.SignupDevice;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
        private Long id;
        @NotBlank(message = "Tên người dùng không được để trống.")
        @Size(min = 3, max = 50, message = "Tên người dùng phải từ 3 đến 50 ký tự.")
        private String username;
        @NotBlank(message = "Email không được để trống.")
        @Email(message = "Email không hợp lệ.")
        private String email;
        private String fullName;
        @Past(message = "Ngày sinh phải là một ngày trong quá khứ.")
        private LocalDate dob;
        @NotBlank(message = "Số điện thoại không được để trống.")
        @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Số điện thoại không hợp lệ.")
        private String phone;
        @URL(message = "URL ảnh đại diện không hợp lệ.")
        private String avatarUrl;
        private SignupDevice signupDevice;
        private MembershipLevel membershipLevel;
        private Boolean isEnabled;
        private Boolean isConfirm;
        private LocalDateTime createdDate;
        private Set<Role> roles = new HashSet<>();

}
