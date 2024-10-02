package com.movie_theaters.email;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Email {

    @NotBlank(message = "email không được để trống")
    private String toEmail;
    @NotBlank(message = "tiêu đề không được để trống")
    private String subjects;
    @NotBlank(message = "body không được để trống")
    private String body;

}
