package com.vladdjuga.blogsite.controller;

import com.vladdjuga.blogsite.model.entity.BlogPostEntity;
import com.vladdjuga.blogsite.service.BlogPostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/blog_post")
class BlogPostController {
    private final BlogPostService postService;
    public BlogPostController(BlogPostService postService) {
        this.postService = postService;
    }

    @GetMapping({"", "/"})
    public String getAll(Model model) {
        model.addAttribute("posts", postService.getAll());
        return "blog_post/index"; // templates/blog_post/index.html
    }

    @GetMapping("/create")
    public String createNewPostPage(Model model) {
        model.addAttribute("blogPost", new BlogPostEntity());
        return "blog_post/create"; // templates/blog_post/create.html
    }

    @PostMapping({"", "/"})
    public String createNewPost(BlogPostEntity post) {
        postService.savePost(post);
        return "redirect:/blog_post/";
    }

}
