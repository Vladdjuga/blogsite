package com.vladdjuga.blogsite.controller;

import com.vladdjuga.blogsite.dto.user.ReadUserDto;
import com.vladdjuga.blogsite.dto.user.UpdateUserDto;
import com.vladdjuga.blogsite.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ReadUserDto> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        var res = userService.getByUsername(userDetails.getUsername());
        if (!res.isSuccess) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(res.value);
    }

    @PutMapping("/me")
    public ResponseEntity<ReadUserDto> updateCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UpdateUserDto user) {
        var res = userService.updateByUsername(userDetails.getUsername(), user);
        if (!res.isSuccess) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(res.value);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        var res = userService.deleteByUsername(userDetails.getUsername());
        if (!res.isSuccess) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
