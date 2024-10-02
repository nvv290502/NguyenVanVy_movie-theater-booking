package com.movie_theaters.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
        private Long id;
        @NotBlank(message = "Tên không được để trống!")
        @Size(max = 255, message = "Tên không được dài quá 255 ký tự!")
        private String name;
        @NotBlank(message = "Mô tả không được để trống!")
        @Size(max = 1000, message = "Mô tả không được dài quá 1000 ký tự!")
        private String description;
        private Boolean isEnable;

}
