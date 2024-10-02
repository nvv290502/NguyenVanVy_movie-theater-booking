package com.movie_theaters.controller.api;

import com.movie_theaters.dto.CategoryDTO;
import com.movie_theaters.dto.MovieDTO;
import com.movie_theaters.dto.response.ApiCollectionResponse;
import com.movie_theaters.dto.response.ApiPagingResponse;
import com.movie_theaters.dto.response.ApiResponse;
import com.movie_theaters.entity.Category;
import com.movie_theaters.repository.CategoryRepository;
import com.movie_theaters.service.CategoryService;
import com.movie_theaters.service.impl.CategoryServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/pub")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryServiceImpl categoryServiceImpl;
    private final CategoryRepository categoryRepository;

    @PostMapping("/category")
    public ResponseEntity<ApiResponse<Category>> saveCategory(@RequestBody @Valid CategoryDTO categoryDTO){
        Category category = categoryService.saveCategory(categoryDTO);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED,category,"Thêm mới category thành công!"), HttpStatus.CREATED);
    }

    @GetMapping("/category")
    public ResponseEntity<ApiPagingResponse<CategoryDTO>> getAllCategory(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "100") int size,
                                                                         @RequestParam(defaultValue = "id") String column,
                                                                         @RequestParam(defaultValue = "asc") String sortType){
        Page<CategoryDTO> categories = categoryServiceImpl.getAll(page, size, column, sortType);
        return new ResponseEntity<>(new ApiPagingResponse<>(HttpStatus.OK.value(),categories,"Trả về danh sách thể loại thành công"), HttpStatus.OK);
    }

    @GetMapping("/category/isEnable")
    public ResponseEntity<ApiPagingResponse<CategoryDTO>> getCategoryIsEnable(@RequestParam(defaultValue = "0") int page,
                                                                              @RequestParam(defaultValue = "100") int size,
                                                                              @RequestParam(defaultValue = "id") String column,
                                                                              @RequestParam(defaultValue = "asc") String sortType){
        Page<CategoryDTO> categories = categoryServiceImpl.getByIsEnabled(page,size,column,sortType);
        return new ResponseEntity<>(new ApiPagingResponse<>(HttpStatus.OK.value(),categories,"Trả về danh sách thể loại thành công"), HttpStatus.OK);
    }

    @PutMapping("/category")
    public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(@RequestBody @Valid CategoryDTO categoryDTO){
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.ACCEPTED,null,categoryService.updateCategory(categoryDTO)), HttpStatus.ACCEPTED);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<ApiResponse<CategoryDTO>> getCategoryById(@PathVariable Long id){
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK, categoryService.findCategoryById(id), "Trả về danh sách phim theo id thành công!"), HttpStatus.OK);
    }
    @PostMapping("/category/isEnable")
    public ResponseEntity<ApiResponse<String>> setIsEnable(@RequestParam Long id, @RequestParam Boolean isEnable){
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.ACCEPTED, null, categoryService.setIsEnableCategory(id, isEnable)), HttpStatus.ACCEPTED);
    }

    @GetMapping("/category/getName")
    public ResponseEntity<ApiResponse<List<String>>> getListNameCategory(){
        List<Category> categories = categoryRepository.findAll();
        List<String> listName = categories.stream().map(c->c.getName()).collect(Collectors.toList());
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK, listName, "Lấy danh sách tên thế loại thành công!"), HttpStatus.OK);
    }

    @GetMapping("/category/search")
    public ResponseEntity<ApiResponse<CategoryDTO>> searchCategoryByName(@RequestParam String name){
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK, categoryService.findCategoryByName(name), "Trả về thể loại theo tên thành công!"), HttpStatus.OK);
    }

}
