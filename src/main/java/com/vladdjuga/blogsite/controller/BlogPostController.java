package com.vladdjuga.blogsite.controller;

import com.vladdjuga.blogsite.dto.blog_post.CreateBlogPostDto;
import com.vladdjuga.blogsite.dto.blog_post.ReadBlogPostDto;
import com.vladdjuga.blogsite.dto.blog_post.UpdateBlogPostDto;
import com.vladdjuga.blogsite.service.BlogPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/{id}")
    public ResponseEntity<ReadBlogPostDto> getById(@PathVariable Long id) {
        var res = postService.getById(id);
        if(!res.isSuccess){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(res.value);
    }

    @PostMapping({"", "/"})
    public ResponseEntity<ReadBlogPostDto> createNewPost(@Valid @RequestBody CreateBlogPostDto post) {
        var res = postService.savePost(post);
        if(!res.isSuccess){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(res.value);
    }

    @PreAuthorize("@securityService.isPostOwner(#id) or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ReadBlogPostDto> updatePost(@PathVariable Long id, @Valid @RequestBody UpdateBlogPostDto post) {
        var res = postService.updatePost(id, post);
        if(!res.isSuccess){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(res.value);
    }

    @PreAuthorize("@securityService.isPostOwner(#id) or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        var res = postService.deletePost(id);
        if(!res.isSuccess){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
