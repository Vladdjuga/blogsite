package com.vladdjuga.blogsite.controller;

import com.vladdjuga.blogsite.dto.user.ReadUserDto;
import com.vladdjuga.blogsite.dto.user.UpdateUserDto;
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

    @GetMapping("/{id}")
    public ResponseEntity<ReadUserDto> getById(@PathVariable Long id) {
        var res = userService.getById(id);
        if(!res.isSuccess){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(res.value);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReadUserDto> updateUser(@PathVariable Long id, @RequestBody UpdateUserDto user) {
        var res = userService.updateUser(id, user);
        if(!res.isSuccess){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(res.value);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        var res = userService.deleteUser(id);
        if(!res.isSuccess){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
