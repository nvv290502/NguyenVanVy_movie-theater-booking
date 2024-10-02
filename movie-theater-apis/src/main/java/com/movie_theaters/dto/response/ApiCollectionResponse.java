package com.movie_theaters.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiCollectionResponse<T> {

    private HttpStatus status;

    private Collection<T> data;

    private String message;


}
