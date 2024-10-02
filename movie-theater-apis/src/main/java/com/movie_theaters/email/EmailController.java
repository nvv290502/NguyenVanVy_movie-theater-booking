package com.movie_theaters.email;

import com.movie_theaters.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pub/email")
public class EmailController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/confirm")
    public String confirmRegistration(@RequestParam("token") String token) {
        return userService.confirmUserRegistration(token);
    }
}
