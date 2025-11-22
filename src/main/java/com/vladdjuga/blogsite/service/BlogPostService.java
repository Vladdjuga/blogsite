package com.vladdjuga.blogsite.service;

import com.vladdjuga.blogsite.annotation.WrapResult;
import com.vladdjuga.blogsite.dto.blog_post.CreateBlogPostDto;
import com.vladdjuga.blogsite.dto.blog_post.ReadBlogPostDto;
import com.vladdjuga.blogsite.mapper.blog_post.BlogPostMapper;
import com.vladdjuga.blogsite.repository.BlogPostRepository;
import com.vladdjuga.blogsite.repository.UserRepository;
import com.vladdjuga.blogsite.result.Result;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogPostService {
    private final BlogPostRepository postRepository;
    private final UserRepository userRepository;
    private final BlogPostMapper blogPostMapper;

    @WrapResult
    public Result<List<ReadBlogPostDto>> getAll(){
        log.info("Getting all blog posts");
        var posts = postRepository.findAll();
        var res = posts.stream().map(blogPostMapper::toDto).toList();
        return Result.ok(res);
    }

    @WrapResult
    @Transactional
    public Result<ReadBlogPostDto> savePost(CreateBlogPostDto post){
        if(post == null){
            log.warn("Attempted to save null post");
            return Result.fail("Post cannot be null");
        }
        log.info("Saving post");
        log.info("Post: {}", post);

        var author = userRepository.findById(post.authorId());
        if(author.isEmpty()){
            log.warn("Author with id {} not found", post.authorId());
            return Result.fail("Author not found");
        }

        var postEntity = blogPostMapper.toEntity(post,author.get());
        var savedEntity = postRepository.saveAndFlush(postEntity);
        var resDto = blogPostMapper.toDto(savedEntity);
        return Result.ok(resDto);
    }
}
