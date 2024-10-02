package com.movie_theaters.controller.api;

import com.movie_theaters.dto.UserDTO;
import com.movie_theaters.dto.request.ChangePasswordRequest;
import com.movie_theaters.dto.request.RegisterRequest;
import com.movie_theaters.dto.request.UpdateUserRequest;
import com.movie_theaters.dto.response.ApiCollectionResponse;
import com.movie_theaters.dto.response.ApiPagingResponse;
import com.movie_theaters.dto.response.ApiResponse;
import com.movie_theaters.dto.response.HistoryOrder;
import com.movie_theaters.entity.enums.MembershipLevel;
import com.movie_theaters.entity.enums.SignupDevice;
import com.movie_theaters.entity.User;
import com.movie_theaters.exception.TokenException;
import com.movie_theaters.repository.UserRepository;
import com.movie_theaters.service.impl.UserServiceImpl;
import com.movie_theaters.service.jwt.JwtService;
import com.movie_theaters.service.UserService;
import com.movie_theaters.util.TokenType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor

public class UserApiController {

    private final UserService userService;
    private final UserServiceImpl userServiceImpl;
    private final UserRepository userRepository;
    private final JwtService jwtService;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@RequestBody @Valid RegisterRequest request){
        User user = userService.register(request);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED,user,"Đăng ký thành công! Vui lòng truy cập email xác thực tài khoản!!"),HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiPagingResponse<UserDTO>> getAll(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "100") int size,
                                                    @RequestParam(defaultValue = "id") String column,
                                                    @RequestParam(defaultValue = "asc") String sortType){
        Page<UserDTO> users = userService.getAll(page, size, column, sortType);
        return new ResponseEntity<>(new ApiPagingResponse<>(HttpStatus.OK.value(), users, "Trả về danh sách phim thành công!"), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        User user = userRepository.findById(id).get();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") @Valid String authorizationHeader) {
        if(authorizationHeader.equals("null")){
            return new ResponseEntity<>("Bạn chưa đăng nhập",HttpStatus.OK);
        }
        String token = authorizationHeader.replace("Bearer ", "");
        String username = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
        Optional<User> user = userRepository.findByUsername(username);
        if(!jwtService.isValidToken(token, TokenType.ACCESS_TOKEN, user.get())){
            throw new TokenException("Toke đã bị thay đổi hoặc hết hạn!");
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user.get(), userDTO);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

//    @PutMapping("/update")
//    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@RequestBody @Valid UserDTO userDTO){
//        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.ACCEPTED,null,userService.updateUser(userDTO)), HttpStatus.ACCEPTED);
//    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email) {
        userServiceImpl.forgotPassword(email);
        return "Mã xác nhận đã được gửi đến email của bạn.";
    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email,
                                           @RequestParam String code,
                                           @RequestParam String password,
                                           @RequestParam String rePassword){
        return new ResponseEntity<>(userServiceImpl.resetPassword(code,password,email,rePassword), HttpStatus.ACCEPTED);
    }

    @GetMapping("/api/pub/amount-money")
    public ResponseEntity<?> getAmountMoney(@RequestParam Long userId){
        return new ResponseEntity<>(userServiceImpl.getAmountMoney(userId), HttpStatus.OK);
    }

    @PutMapping("/api/pub/update")
    public ResponseEntity<?> updateUserInfo(@ModelAttribute @Valid UpdateUserRequest request,
                                            @RequestParam(required = false) MultipartFile avatarUrl){
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.ACCEPTED, userService.updateUserInfo(request, avatarUrl),"Cập nhật thành công!"), HttpStatus.ACCEPTED);
    }

    @PutMapping("/api/pub/change-password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest request){
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.ACCEPTED, userService.changePassword(request),"Thay đổi mật khẩu thành công!"), HttpStatus.ACCEPTED);
    }

    @GetMapping("api/pub/history-order")
    public ResponseEntity<?> getHistoryBill(@RequestParam(required = false) Long userId,
                                            @RequestParam(required = false) LocalDate date,
                                            @RequestParam(required = false) String billCode,
                                            @PageableDefault(size = 6) Pageable pageable){
        Page<HistoryOrder> historyOrderList = userService.getHistoryBill(userId, date, billCode, pageable);
        return new ResponseEntity<>(new ApiPagingResponse<>(HttpStatus.OK.value(), historyOrderList, "Danh sách lịch sử đơn hàng!"), HttpStatus.OK);
    }
}
