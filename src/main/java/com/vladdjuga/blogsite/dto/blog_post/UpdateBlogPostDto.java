package com.vladdjuga.blogsite.dto.blog_post;

import jakarta.validation.constraints.Size;

public record UpdateBlogPostDto(
        @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
        String title,

        String content
) {}

