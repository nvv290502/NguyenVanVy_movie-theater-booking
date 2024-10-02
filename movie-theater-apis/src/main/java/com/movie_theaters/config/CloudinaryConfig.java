package com.movie_theaters.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "duqypb9ee",
                "api_key", "721641816111285",
                "api_secret", "XWq9OQaM-FP91VOuYeT89c30LAE",
                "secure", true
        ));
    }
}
