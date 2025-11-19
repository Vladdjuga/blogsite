package com.vladdjuga.blogsite.controller;

import com.vladdjuga.blogsite.model.entity.UserEntity;
import com.vladdjuga.blogsite.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"", "/"})
    public List<UserEntity> getAll() {
        return userService.getAll();
    }

    @PostMapping({"", "/"})
    public ResponseEntity<UserEntity> createNewUser(@RequestBody UserEntity user) {
        userService.saveUser(user);
        return ResponseEntity.ok(user);
    }
}
