package com.movie_theaters;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MovieTheatersApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieTheatersApplication.class, args);
	}

}
