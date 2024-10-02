package com.movie_theaters.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieSales {
    private Long movieId;
    private Integer numberTicket;
}
