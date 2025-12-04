package com.vladdjuga.blogsite.dto.blog_post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateBlogPostDto(
        @NotBlank(message = "Title is required")
        @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
        String title,

        @NotBlank(message = "Content is required")
        String content,

        @NotNull(message = "Author ID is required")
        Long authorId
) {}
