package com.vladdjuga.blogsite.controller;

import com.vladdjuga.blogsite.model.entity.BlogPostEntity;
import com.vladdjuga.blogsite.service.BlogPostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class BlogPostController {
    private final BlogPostService postService;
    public BlogPostController(BlogPostService postService) {
        this.postService = postService;
    }

    @GetMapping({"", "/"})
    public List<BlogPostEntity> getAll() {
        return postService.getAll();
    }

    @PostMapping({"", "/"})
    public ResponseEntity<BlogPostEntity> createNewPost(@RequestBody BlogPostEntity post) {
        postService.savePost(post);
        return ResponseEntity.ok(post);
    }

}
