package com.movie_theaters.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDTO {
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

        private Boolean isEnable;

        private String status;

        private String imageSmallUrl;

        private String imageLargeUrl;

        private List<Long> categoryId;

}
