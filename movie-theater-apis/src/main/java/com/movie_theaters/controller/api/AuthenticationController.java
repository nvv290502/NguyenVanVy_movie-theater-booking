package com.movie_theaters.controller.api;

import com.movie_theaters.dto.request.LoginRequest;
import com.movie_theaters.dto.response.ErrorApiResponse;
import com.movie_theaters.dto.response.TokenResponse;
import com.movie_theaters.entity.User;
import com.movie_theaters.repository.UserRepository;
import com.movie_theaters.service.UserService;
import com.movie_theaters.service.jwt.AuthenticationService;
import com.movie_theaters.service.jwt.JwtService;
import com.movie_theaters.util.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Validated
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "success";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername()).get();
        if (!user.getIsConfirm()) {
            throw new RuntimeException("Tài khoản chưa xác thực!");
        }
        try {
            TokenResponse tokenResponse = authenticationService.authentication(loginRequest);
            return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
        } catch (UsernameNotFoundException ex) {
            return new ResponseEntity<>(
                    new ErrorApiResponse(HttpStatus.NOT_FOUND.value(), "User not found", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        } catch (BadCredentialsException ex) {
            return new ResponseEntity<>(new ErrorApiResponse(HttpStatus.UNAUTHORIZED.value(), "Unauthorized",
                    "Tài khoản hoặc mật khẩu không chính xác!"), HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ErrorApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Internal Server Error", "Đã xảy ra lỗi"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid HttpServletRequest request) {
        return new ResponseEntity<>(authenticationService.refresh(request), HttpStatus.OK);
    }

    @GetMapping("/admin")
    public ResponseEntity<String> getAdmin(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vui lòng đăng nhập!");
        }
        String userName = jwtService.extractUsername(accessToken, TokenType.ACCESS_TOKEN);
        Optional<User> user = userRepository.findByUsername(userName);
        if (!jwtService.isValidToken(accessToken, TokenType.ACCESS_TOKEN, user.get())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token không hợp lệ");
        }
        System.out.println(user.get().getAuthorities());
        if (user.get().getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.ok("OK"); // Trả về nội dung trang admin
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Không có quyền truy cập");
        }
    }

    @GetMapping("/validate-token")
    public ResponseEntity<String> validateToken(@RequestParam String token) {
        // Lấy username từ token (để sử dụng cho kiểm tra)
        String username = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);

        // Tìm UserDetails từ username
        UserDetails userDetails = userService.loadUserByUsername(username);

        // Kiểm tra tính hợp lệ của token
        boolean isValid = jwtService.isValidToken(token, TokenType.ACCESS_TOKEN, userDetails);

        if (isValid) {
            return ResponseEntity.ok("Token is valid");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid");
        }
    }

}
