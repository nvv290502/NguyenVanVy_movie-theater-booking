package com.movie_theaters.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data@NoArgsConstructor@AllArgsConstructor
public class CinemaRequest {

    @NotBlank(message = "Tên rạp không được để trống")
    @Size(max = 100, message = "Tên rạp không được vượt quá 100 ký tự")
    private String name;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(max = 500, message = "Địa chỉ không được vượt quá 500 ký tự")
    private String address;

    @NotBlank(message = "Hotline không được để trống")
    @Pattern(regexp = "^0\\d{9,10}$", message = "Số điện thoại không hợp lệ!")
    private String hotline;

    @NotBlank(message = "Mô tả không được để trống")
    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    private String description;

    private Boolean isEnabled;
}
