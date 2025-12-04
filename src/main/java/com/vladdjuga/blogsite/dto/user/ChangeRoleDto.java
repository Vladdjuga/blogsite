package com.vladdjuga.blogsite.dto.user;

import com.vladdjuga.blogsite.model.Role;
import jakarta.validation.constraints.NotNull;

public record ChangeRoleDto(
        @NotNull(message = "Role is required")
        Role role
) {}

