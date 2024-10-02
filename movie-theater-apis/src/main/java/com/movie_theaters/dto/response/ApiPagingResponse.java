package com.movie_theaters.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiPagingResponse<T>{
    private int status;
    private Page<T> data;
    private String message;
}
