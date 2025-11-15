package com.vladdjuga.blogsite.controller;

import com.vladdjuga.blogsite.service.BlogPostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
class BlogPostController {
    private final BlogPostService postService;
    public BlogPostController(BlogPostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public String posts(Model model) {
        model.addAttribute("posts", postService.getAll());
        return "blog_post/posts"; // templates/blog_post/posts.html
    }

}
