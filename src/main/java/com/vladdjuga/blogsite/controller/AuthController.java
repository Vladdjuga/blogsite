package com.vladdjuga.blogsite.controller;


import com.vladdjuga.blogsite.dto.user.LoginUserDto;
import com.vladdjuga.blogsite.dto.user.RegisterUserDto;
import com.vladdjuga.blogsite.dto.user.ReadUserDto;
import com.vladdjuga.blogsite.service.AuthService;
import com.vladdjuga.blogsite.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @Value("${jwt.expirationMs}")
    private long jwtExpirationMs;
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginUserDto req) {
        var res = authService.authenticate(req.username(), req.password());
        if(!res.isSuccess){
            return ResponseEntity.badRequest().build();
        }
        ResponseCookie cookie = ResponseCookie.from("accessToken", res.value)
                .httpOnly(true)       // secure from XSS attacks
                .secure(false)
                .path("/")
                .maxAge(jwtExpirationMs / 1000)  // convert ms to seconds
                .sameSite("Strict")   // secure from CSRF attacks
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logged in successfully");
    }

    @PostMapping("/register")
    public ResponseEntity<ReadUserDto> register(@Valid @RequestBody RegisterUserDto user) {
        var res = userService.saveUser(user);
        if(!res.isSuccess){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(res.value);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        ResponseCookie cookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)  // immediately expire the cookie
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logged out successfully");
    }
}
