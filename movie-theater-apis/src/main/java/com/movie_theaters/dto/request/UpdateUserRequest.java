package com.movie_theaters.dto.request;

import com.movie_theaters.entity.enums.MembershipLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {
    private Long id;
    private String fullName;
    private String phone;
    private LocalDate dob;
    private MembershipLevel membershipLevel;
}
