package com.movie_theaters.dto.request;

import com.movie_theaters.entity.Role;
import com.movie_theaters.entity.enums.MembershipLevel;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
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
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Số điện thoại không hợp lệ.")
    private String phone;
    private MembershipLevel membershipLevel;
    private Boolean isConfirm;
    private String roleName;
}
