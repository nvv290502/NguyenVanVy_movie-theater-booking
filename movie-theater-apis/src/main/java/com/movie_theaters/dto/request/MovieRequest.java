package com.movie_theaters.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieRequest {
    private Long id;

    @NotBlank(message = "Tên không được để trống!")
    private String name;

    @NotBlank(message = "Tóm tắt không được để trống!")
    private String summary;

    @NotNull(message = "Thời lượng không được để trống!")
    @Positive(message = "Thời lượng phải là số dương!")
    private Integer duration;

    @NotNull(message = "Ngày phát hành không được để trống!")
    private LocalDate releasedDate;

    @NotBlank(message = "Tác giả không được để trống!")
    private String author;

    @NotBlank(message = "Diễn viên không được để trống!")
    private String actor;

    @NotBlank(message = "Ngôn ngữ không được để trống!")
    private String language;

    @NotBlank(message = "Trailer không được để trống!")
    private String trailerUrl;

//    @NotNull(message = "Ảnh không được để trống")
//    private MultipartFile imageSmall;
//
//    @NotNull(message = "Ảnh không được để trống")
//    private MultipartFile imageLarge;

    @NotBlank(message = "Category không được để trống!")
    private String categoryId;

    private String imageSmallUrl;

    private String imageLargeUrl;

}
