package com.vladdjuga.blogsite.service;

import com.vladdjuga.blogsite.model.entity.BlogPostEntity;
import com.vladdjuga.blogsite.repository.BlogPostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BlogPostService {
    private final BlogPostRepository postRepository;
    public BlogPostService(BlogPostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<BlogPostEntity> getAll(){
        log.info("Getting all posts");
        return postRepository.findAll();
    }

    public BlogPostEntity savePost(BlogPostEntity post){
        log.info("Saving post");
        log.info("Post: {}", post);
        return postRepository.save(post);
    }
}
