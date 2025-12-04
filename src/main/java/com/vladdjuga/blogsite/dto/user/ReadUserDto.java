package com.vladdjuga.blogsite.dto.user;

import com.vladdjuga.blogsite.model.Role;

import java.util.Date;

public record ReadUserDto(Long id, String username, String email, Role role, Date createdAt) {}
