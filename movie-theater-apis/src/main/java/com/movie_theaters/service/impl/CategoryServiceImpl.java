package com.movie_theaters.service.impl;

import com.movie_theaters.dto.CategoryDTO;
import com.movie_theaters.dto.MovieDTO;
import com.movie_theaters.entity.Category;
import com.movie_theaters.entity.Movie;
import com.movie_theaters.exception.EmptyListException;
import com.movie_theaters.exception.ExistObjectException;
import com.movie_theaters.repository.CategoryRepository;
import com.movie_theaters.service.CategoryService;
import com.movie_theaters.service.generic.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional

public class CategoryServiceImpl extends Pagination<Category, Long, CategoryDTO> implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    protected CategoryServiceImpl(JpaRepository<Category, Long> repository) {
        super(repository);
    }
    @Override
    public Category saveCategory(CategoryDTO categoryDTO) {
        if(categoryRepository.existsByName(categoryDTO.getName())){
            throw new ExistObjectException("Thể loại đã tồn tại");
        }
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        category.setIsEnable(true);
        return categoryRepository.save(category);
    }

    @Override
    public String updateCategory(CategoryDTO categoryDTO) {
        Optional<Category> category = categoryRepository.findById(categoryDTO.getId());
        if(category.isEmpty()){
            throw new EmptyListException("Thể loại không tồn tại!");
        }
        BeanUtils.copyProperties(categoryDTO, category.get());
        return "Cập nhật thể loại thành công";
    }

    @Override
    protected CategoryDTO convert(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        BeanUtils.copyProperties(category, categoryDTO);
        return categoryDTO;
    }

    @Override
    protected Page<Category> getIsEnabledMethod(PageRequest pageRequest, Boolean isEnable) {
        return categoryRepository.getByIsEnable(pageRequest, isEnable);
    }

    @Override
    public CategoryDTO findCategoryById(Long id) {
        CategoryDTO categoryDTO = new CategoryDTO();
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()){
            throw new EmptyListException("Thể loại không tồn tại!");
        }
        BeanUtils.copyProperties(category.get(), categoryDTO);
        return categoryDTO;
    }

    @Override
    public String setIsEnableCategory(Long id,Boolean isEnable) {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()){
            throw new EmptyListException("Id không tồn tài");
        }
        category.get().setIsEnable(isEnable);
        categoryRepository.save(category.get());
        if(!isEnable){
            return "Cập nhật trạng thái DISABLE thể loại có id ["+id+"] thành công!";
        }else {
            return "Cập nhật trạng thái ENABLE thể loại có id ["+id+"] thành công!";

        }
    }

    @Override
    public CategoryDTO findCategoryByName(String name) {
        CategoryDTO categoryDTO = new CategoryDTO();
        if(name.isBlank()){
            throw new EmptyListException("Tên thể loại không được để trống!");
        }
        Category category = categoryRepository.getByName(name);
        BeanUtils.copyProperties(category, categoryDTO);
        return categoryDTO;
    }
}
