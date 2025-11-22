package com.vladdjuga.blogsite.dto.user;

import java.util.Date;

public record ReadUserDto(Long id, String username, String email, Date createdAt) {}
