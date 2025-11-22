package com.vladdjuga.blogsite.dto.blog_post;

import java.util.Date;

public record ReadBlogPostDto(Long id, String title, String content, Long authorId, Date createdAt) {}
