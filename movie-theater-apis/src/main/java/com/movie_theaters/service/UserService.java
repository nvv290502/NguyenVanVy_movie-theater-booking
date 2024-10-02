package com.movie_theaters.service;

import com.movie_theaters.dto.UserDTO;
import com.movie_theaters.dto.request.ChangePasswordRequest;
import com.movie_theaters.dto.request.RegisterRequest;
import com.movie_theaters.dto.request.UpdateUserRequest;
import com.movie_theaters.dto.request.UserRequest;
import com.movie_theaters.dto.response.HistoryOrder;
import com.movie_theaters.entity.User;
import com.movie_theaters.entity.enums.StatusBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface UserService {
    User register(RegisterRequest request);

    User createCustomer(UserRequest userRequest, String password, MultipartFile avatar);

    void addRoleToUser(String username, String roleName);

    Page<UserDTO> getAll(int page, int size, String column, String sortType);

    User updateUserAdmin(UserRequest userRequest, MultipartFile avatar);

    UserDetailsService userDetailsService();

    User findOrCreateUser(String email);
    UserDTO findById(Long id);

    String updateIsEnable(Long id, Boolean isEnable);
    UserDTO searchAccountByName(String name);

    UserDetails loadUserByUsername(String username);

    Float getAmountMoney(Long userId);

    User updateUserInfo(UpdateUserRequest request, MultipartFile avatarUrl);
    User changePassword(ChangePasswordRequest request);
    Page<HistoryOrder> getHistoryBill(Long userId,LocalDate date,String billCode, Pageable pageable);

}
