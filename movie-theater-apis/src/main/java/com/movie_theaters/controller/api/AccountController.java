package com.movie_theaters.controller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie_theaters.dto.CategoryDTO;
import com.movie_theaters.dto.MovieDTO;
import com.movie_theaters.dto.UserDTO;
import com.movie_theaters.dto.request.MovieRequest;
import com.movie_theaters.dto.request.UserRequest;
import com.movie_theaters.dto.response.ApiResponse;
import com.movie_theaters.dto.response.ErrorApiResponse;
import com.movie_theaters.entity.Category;
import com.movie_theaters.entity.Role;
import com.movie_theaters.entity.User;
import com.movie_theaters.exception.EmptyListException;
import com.movie_theaters.repository.RoleRepository;
import com.movie_theaters.repository.UserRepository;
import com.movie_theaters.service.UserService;
import com.movie_theaters.service.impl.ImageUploadService;
import com.movie_theaters.service.jwt.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/account")
@Validated
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AccountController {
    private final UserService userService;
    @PostMapping("/create/customer")
    public ResponseEntity<?> createCustomer(@ModelAttribute @Valid UserRequest userRequest,
                                            @RequestParam(value = "avatarUrl", required = false) MultipartFile avatarUrl,
                                            @RequestParam String password){
        User user = userService.createCustomer(userRequest, password, avatarUrl);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED, user, "Tạo tài khoản khách hàng thành công. Kiểm tra mail để xác nhận tài khoản!"), HttpStatus.CREATED);
    }
    @PutMapping("/update/customer")
    public ResponseEntity<?> updateCustomer(@ModelAttribute @Valid UserRequest userRequest,
                                            @RequestParam(value = "avatarUrl", required = false) MultipartFile avatarUrl){
        User user = userService.updateUserAdmin(userRequest, avatarUrl);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.ACCEPTED,user,"Cập nhật thông tin khách hàng thành công!"), HttpStatus.ACCEPTED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable @Valid Long id){
        if(id == null || id < 0){
            return new ResponseEntity<>("Id không hợp lệ!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK, userService.findById(id), "Trả về user theo id thành công!"), HttpStatus.OK);
    }
    @PostMapping("/isEnable")
    public ResponseEntity<ApiResponse<String>> setIsEnable(@RequestParam Long id, @RequestParam Boolean isEnable){
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.ACCEPTED, null, userService.updateIsEnable(id, isEnable)), HttpStatus.ACCEPTED);
    }
}
