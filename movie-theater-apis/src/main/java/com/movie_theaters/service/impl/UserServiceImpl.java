package com.movie_theaters.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie_theaters.dto.UserDTO;
import com.movie_theaters.dto.request.*;
import com.movie_theaters.dto.response.HistoryOrder;
import com.movie_theaters.email.EmailService;
import com.movie_theaters.entity.enums.MembershipLevel;
import com.movie_theaters.entity.enums.SignupDevice;
import com.movie_theaters.entity.Category;
import com.movie_theaters.entity.Movie;
import com.movie_theaters.entity.Role;
import com.movie_theaters.entity.User;
import com.movie_theaters.entity.enums.StatusBill;
import com.movie_theaters.exception.EmptyListException;
import com.movie_theaters.exception.ExistObjectException;
import com.movie_theaters.exception.ObjectNotFoundException;
import com.movie_theaters.repository.RoleRepository;
import com.movie_theaters.repository.UserRepository;
import com.movie_theaters.service.jwt.JwtService;
import com.movie_theaters.service.UserService;
import com.movie_theaters.service.generic.Pagination;
import com.movie_theaters.util.TokenType;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    private static final String DIGITS = "0123456789";
    private static final int CODE_LENGTH = 5;
    private static SecureRandom random = new SecureRandom();
    private static ConcurrentMap<String, String> codeStorage = new ConcurrentHashMap<>();
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final ImageUploadService imageUploadService;

    @Autowired
    protected UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtService jwtService, EmailService emailService, ImageUploadService imageUploadService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.imageUploadService = imageUploadService;
    }

    @Override
    public User register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại!");
        }

        if (!request.getPassword().equals(request.getRePassword())) {
            throw new IllegalArgumentException("Mật khẩu không đúng!");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsEnabled(true);
        user.setIsConfirm(false);
        userRepository.save(user);

        String token = jwtService.generateToken(user);
        String confirmationUrl = "http://localhost:8080/api/pub/email/confirm?token=" + token;
        emailService.sendEmail(user.getEmail(), user, confirmationUrl);
        return user;
    }

    @Override
    public User createCustomer(UserRequest userRequest, String password, MultipartFile avatar) {
        if(password == null || password.isEmpty()){
            throw new IllegalArgumentException("Mật khẩu không được để trống!");
        }
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại!");
        }

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại!");
        }
        userRequest.setRoleName("ROLE_USER");
        userRequest.setMembershipLevel(MembershipLevel.BASIC);
        userRequest.setIsConfirm(false);
        User user = new User();
        if(avatar != null){
            try {
                user.setAvatarUrl(imageUploadService.uploadImage(avatar));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            user.setAvatarUrl("https://i.pinimg.com/236x/5e/e0/82/5ee082781b8c41406a2a50a0f32d6aa6.jpg");
        }
        BeanUtils.copyProperties(userRequest, user);
        user.setIsEnabled(true);
        Role role = roleRepository.findByRoleName(userRequest.getRoleName());
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        String token = jwtService.generateToken(user);
        String confirmationUrl = "http://localhost:8080/api/pub/email/confirm?token=" + token;
        emailService.sendEmail(user.getEmail(), user, confirmationUrl);
        return user;
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        if(username == null || roleName == null){
            throw new IllegalArgumentException("username or roleName not null");
        }
        User user = userRepository.findByUsername(username).get();
        Role role = roleRepository.findByRoleName(roleName);
        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Override
    public Page<UserDTO> getAll(int page, int size, String column, String sortType) {
        Sort sort = Sort.by(column);
        if("desc".equalsIgnoreCase(sortType)){
            sort = sort.descending();
        }else{
            sort = sort.ascending();
        }
        String roleName = "ROLE_USER";
        Page<User> users = userRepository.findAll(PageRequest.of(page, size ,sort), roleName);
        return users.map(this::convert);
    }

    public UserDTO convert(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDetailsService().loadUserByUsername(username);
    }

    @Override
    public User findOrCreateUser(String email) {
        return userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(email); // Use email as username or modify as needed
            newUser.setPassword(passwordEncoder.encode("defaultPassword")); // Set a default password or use another mechanism
            return userRepository.save(newUser);
        });
    }

    @Override
    public UserDTO findById(Long id) {
        UserDTO userDTO = new UserDTO();
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new EmptyListException("account không tồn tại!");
        }
        BeanUtils.copyProperties(user.get(), userDTO);
        if(user.get().getMembershipLevel() != null){
            userDTO.setMembershipLevel(user.get().getMembershipLevel());
        }
        userDTO.setRoles(
                user.get().getRoles()
        );
        return userDTO;
    }

    @Override
    public User updateUserAdmin(UserRequest userRequest, MultipartFile avatar){
        if(userRequest.getId() == null){
            throw new NullPointerException("Bạn chưa nhập id");
        }
        Optional<User> user = userRepository.findById(userRequest.getId());
        if(user.isEmpty()){
            throw new ObjectNotFoundException("Người dùng không tồn tại");
        }
        if(avatar != null){
            try {
                user.get().setAvatarUrl(imageUploadService.uploadImage(avatar));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        BeanUtils.copyProperties(userRequest, user.get());
        Role role = roleRepository.findByRoleName(userRequest.getRoleName());
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.get().setRoles(roles);
        userRepository.save(user.get());
        return user.get();
    }

    public String confirmUserRegistration(String token) {
        try {
            String username = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
            System.out.println("username" + username);
            User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
            if (jwtService.isValidToken(token, TokenType.ACCESS_TOKEN, user)) {
                user.setIsConfirm(true);
                user.setSignupDevice(SignupDevice.LOCAL);
                user.setMembershipLevel(MembershipLevel.BASIC);
                addRoleToUser(username, "ROLE_USER");
                userRepository.save(user);
                return "Xác nhận thành công! <a href=\"http://localhost:3000/login\">Quay lại trang đăng nhập</a>.";
            } else {
                return "Token không hợp lệ hoặc đã hết hạn.";
            }
        } catch (Exception e) {
            return "Xác nhận không thành công: " + e.getMessage();
        }
    }

    @Scheduled(cron = "0 * * * * *")
    public void deleteUnconfirmedUsers() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cutoff = now.minusMinutes(1);
        List<User> usersToDelete = userRepository.findByIsConfirmFalseAndCreatedDateBefore(cutoff);
        log.info("Xóa tài khoản chưa xác nhận! {}", now);
        userRepository.deleteAll(usersToDelete);
    }

    public static String generateCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(DIGITS.length());
            code.append(DIGITS.charAt(index));
        }
        return code.toString();
    }

    public void forgotPassword(String email){
        String code = generateCode();
        if(!userRepository.existsByEmail(email)){
            throw new NullPointerException("Email không tồn tại!");
        }
        codeStorage.put(email, code);
        emailService.sendForgotPassword(email, code);
    }

    public String resetPassword(String code, String password, String email, String rePassword){
        if(!codeStorage.get(email).equals(code)){
            throw new IllegalArgumentException("Mã xác nhận không chính xác!");
        }
        if(!password.equals(rePassword)){
            throw new IllegalArgumentException("Mật khẩu không trùng khớp!");
        }

        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()){
            throw new EmptyListException("Email không chính xác!");
        }
        user.get().setPassword(passwordEncoder.encode(password));
        userRepository.save(user.get());

        codeStorage.remove(email);
        return "Thay đổi mật khẩu thành công!";
    }

    @Override
    public UserDTO searchAccountByName(String name) {
        UserDTO userDTO = new UserDTO();
        if(name.isBlank()){
            throw new EmptyListException("Tên tài khoản không được để trống!");
        }
        Optional<User> user = userRepository.findByUsername(name);
        BeanUtils.copyProperties(user.get(), userDTO);
        return userDTO;
    }

    @Override
    public String updateIsEnable(Long id, Boolean isEnable) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new EmptyListException("Id không tồn tài");
        }
        user.get().setIsEnabled(isEnable);
        userRepository.save(user.get());
        if(!isEnable){
            return "Cập nhật trạng thái DISABLE user có id ["+id+"] thành công!";
        }else {
            return "Cập nhật trạng thái ENABLE user có id ["+id+"] thành công!";

        }
    }

    @Override
    public Float getAmountMoney(Long userId) {
        if(userId == null){
            throw new NullPointerException("Người dùng không tồn tại!");
        }
        return userRepository.getAmountMoney(userId, StatusBill.PAID);
    }

    @Override
    public User updateUserInfo(UpdateUserRequest request, MultipartFile avatarUrl) {
        Optional<User> user = userRepository.findById(request.getId());
        if(user.isEmpty()){
            throw new ObjectNotFoundException("Người dùng không tồn tại!");
        }
        if(avatarUrl != null){
            try {
                user.get().setAvatarUrl(imageUploadService.uploadImage(avatarUrl));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        user.get().setFullName(request.getFullName());
        user.get().setMembershipLevel(request.getMembershipLevel());
        user.get().setDob(request.getDob());
        user.get().setPhone(request.getPhone());

        return userRepository.save(user.get());
    }

    @Override
    public User changePassword(ChangePasswordRequest request) {
        Optional<User> user = userRepository.findByUsername(request.getUsername());
        if(user.isEmpty()){
            throw new ObjectNotFoundException("Người dùng không tồn tại!");
        }

        if(!passwordEncoder.matches(request.getOldPassword(), user.get().getPassword())){
            throw new IllegalArgumentException("Mật khẩu cũ không chính xác!");
        }

        if(!request.getNewPassword().equals(request.getRePassword())){
            throw new IllegalArgumentException("Mật khẩu không trùng khớp!");
        }

        user.get().setPassword(passwordEncoder.encode(request.getNewPassword()));

        return userRepository.save(user.get());
    }

    @Override
    public Page<HistoryOrder> getHistoryBill(Long userId, LocalDate date,String billCode, Pageable pageable) {
        return userRepository.getHistoryBill(userId,date,billCode, pageable);
    }
}
