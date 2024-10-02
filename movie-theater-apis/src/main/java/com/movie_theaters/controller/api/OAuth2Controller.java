package com.movie_theaters.controller.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.movie_theaters.dto.UserDTO;
import com.movie_theaters.entity.enums.MembershipLevel;
import com.movie_theaters.entity.enums.SignupDevice;
import com.movie_theaters.entity.User;
import com.movie_theaters.exception.ObjectNotFoundException;
import com.movie_theaters.repository.RoleRepository;
import com.movie_theaters.repository.UserRepository;
import com.movie_theaters.service.oauth2.OAuth2Service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/oauth2")
@CrossOrigin(origins = "*")
public class OAuth2Controller {

    private static final SecureRandom random = new SecureRandom();

    @Autowired
    private OAuth2Service oAuth2Service;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/getUser")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String authorizationHeader) {
        String accessToken = authorizationHeader.replace("Bearer ", "");
        try {
            UserDTO userDTO = oAuth2Service.getUserInfo(accessToken);
            if(!userRepository.existsByEmail(userDTO.getEmail())){
                User user = new User();
                BeanUtils.copyProperties(userDTO, user);
                user.getRoles().add(roleRepository.findByRoleName("ROLE_USER"));
                user.setSignupDevice(SignupDevice.GOOGLE);
                user.setMembershipLevel(MembershipLevel.BASIC);
                user.setIsConfirm(true);
                user.setIsEnabled(true);
                user.setUsername(generateRandomUsername("google"));
                userRepository.save(user);
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
            else{
                Optional<User> user = userRepository.findByEmail(userDTO.getEmail());
                if(user.isEmpty()){
                    throw new ObjectNotFoundException("Người dùng không tồn tại!");
                }
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    private String generateRandomUsername(String base) {
        String randomNumber = String.format("%05d", random.nextInt(100000));
        return base + randomNumber;
    }

    @GetMapping("/access")
    public ResponseEntity<?> handleGenerateToken(@RequestParam(name = "code", required = false) String code) {
        if (code != null) {
            try {
                String accessToken = oAuth2Service.getAccessToken(code);

                Map<String, String> response = new HashMap<>();
                response.put("access_token", accessToken);

                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (JsonProcessingException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>("code không hợp lệ", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/validate-token")
    public ResponseEntity<String> validateToken(@RequestParam String accessToken) {
        boolean isValid = oAuth2Service.isValidToken(accessToken);
        if (isValid) {
            return ResponseEntity.ok("Token is valid");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid");
        }
    }
}
