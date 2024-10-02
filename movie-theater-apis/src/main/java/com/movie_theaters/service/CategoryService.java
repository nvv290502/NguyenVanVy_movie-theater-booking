package com.movie_theaters.service;

import com.movie_theaters.dto.CategoryDTO;
import com.movie_theaters.dto.MovieDTO;
import com.movie_theaters.entity.Category;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {
    Category saveCategory(CategoryDTO categoryDTO);
//    Page<Category> getAlLCategory(int page, int size, String column, String sortType);
//
//    Page<Category> getAllCategoryIsEnable(int page, int size, String column, String sortType);

    String updateCategory(CategoryDTO categoryDTO);

    CategoryDTO findCategoryById(Long id);

    String setIsEnableCategory(Long id, Boolean isEnable);

    CategoryDTO findCategoryByName(String name);
}
