package com.vladdjuga.blogsite.controller;

import com.vladdjuga.blogsite.dto.blog_post.CreateBlogPostDto;
import com.vladdjuga.blogsite.dto.blog_post.ReadBlogPostDto;
import com.vladdjuga.blogsite.service.BlogPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class BlogPostController {
    private final BlogPostService postService;

    @GetMapping({"", "/"})
    public ResponseEntity<List<ReadBlogPostDto>> getAll() {
        var blogPostsResult = postService.getAll();
        if(!blogPostsResult.isSuccess){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(blogPostsResult.value);
    }

    @PostMapping({"", "/"})
    public ResponseEntity<ReadBlogPostDto> createNewPost(@RequestBody CreateBlogPostDto post) {
        var res = postService.savePost(post);
        if(!res.isSuccess){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(res.value);
    }

}
