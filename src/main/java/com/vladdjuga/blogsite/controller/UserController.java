package com.vladdjuga.blogsite.controller;

import com.vladdjuga.blogsite.dto.user.CreateUserDto;
import com.vladdjuga.blogsite.dto.user.ReadUserDto;
import com.vladdjuga.blogsite.model.entity.UserEntity;
import com.vladdjuga.blogsite.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping({"", "/"})
    public ResponseEntity<List<ReadUserDto>> getAll() {
        var users = userService.getAll();
        if(!users.isSuccess){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(users.value);
    }

    @PostMapping({"", "/"})
    public ResponseEntity<ReadUserDto> createNewUser(@RequestBody CreateUserDto user) {
        var res = userService.saveUser(user);
        if(!res.isSuccess){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(res.value);
    }
}
