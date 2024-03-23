package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.repos.RoleRepository;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginController {
    private final UserService userService;

    @Autowired
    private RoleRepository roleRepository;


    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/sign_in_")
    public boolean addUser(@RequestBody User user, @RequestParam Map<String, String> names){
        return userService.saveUser(user, names);
    }
}
