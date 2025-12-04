package com.vladdjuga.blogsite.controller;

import com.vladdjuga.blogsite.dto.user.ChangeRoleDto;
import com.vladdjuga.blogsite.dto.user.ReadUserDto;
import com.vladdjuga.blogsite.dto.user.UpdateUserDto;
import com.vladdjuga.blogsite.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<ReadUserDto>> getAllUsers() {
        var res = userService.getAll();
        if (!res.isSuccess) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(res.value);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ReadUserDto> getUserById(@PathVariable Long id) {
        var res = userService.getById(id);
        if (!res.isSuccess) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(res.value);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ReadUserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserDto user) {
        var res = userService.updateUser(id, user);
        if (!res.isSuccess) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(res.value);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        var res = userService.deleteUser(id);
        if (!res.isSuccess) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/users/{id}/role")
    public ResponseEntity<ReadUserDto> changeRole(@PathVariable Long id, @Valid @RequestBody ChangeRoleDto dto) {
        var res = userService.changeRole(id, dto.role());
        if (!res.isSuccess) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(res.value);
    }
}

