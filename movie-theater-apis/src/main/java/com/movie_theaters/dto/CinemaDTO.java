package com.movie_theaters.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CinemaDTO {

    private Long id;
    @NotBlank(message = "Tên rạp không được để trống")
    @Size(max = 100, message = "Tên rạp không được vượt quá 100 ký tự")
    private String name;

    @NotBlank(message = "URL hình ảnh không được để trống")
    private String imageUrl;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(max = 500, message = "Địa chỉ không được vượt quá 500 ký tự")
    private String address;

    @NotBlank(message = "Hotline không được để trống")
    @Pattern(regexp = "\\d{10}", message = "Hotline phải gồm 10 chữ số")
    private String hotline;

    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    private String description;

    @NotNull(message = "Trạng thái hoạt động không được để trống")
    private Boolean isEnabled;
}
