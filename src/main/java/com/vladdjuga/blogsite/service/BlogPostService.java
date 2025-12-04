package com.vladdjuga.blogsite.service;

import com.vladdjuga.blogsite.annotation.WrapResult;
import com.vladdjuga.blogsite.dto.blog_post.CreateBlogPostDto;
import com.vladdjuga.blogsite.dto.blog_post.ReadBlogPostDto;
import com.vladdjuga.blogsite.dto.blog_post.UpdateBlogPostDto;
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
    public Result<ReadBlogPostDto> getById(Long id){
        log.info("Getting blog post by id: {}", id);
        var post = postRepository.findById(id);
        if(post.isEmpty()){
            log.warn("Blog post with id {} not found", id);
            return Result.fail("Blog post not found");
        }
        return Result.ok(blogPostMapper.toDto(post.get()));
    }

    @WrapResult
    @Transactional
    public Result<ReadBlogPostDto> savePost(CreateBlogPostDto post,Long authorId){
        if(post == null){
            log.warn("Attempted to save null post");
            return Result.fail("Post cannot be null");
        }
        log.info("Saving post");
        log.info("Post: {}", post);

        var author = userRepository.findById(authorId);
        if(author.isEmpty()){
            log.warn("Author with id {} not found", authorId);
            return Result.fail("Author not found");
        }

        var postEntity = blogPostMapper.toEntity(post,author.get());
        var savedEntity = postRepository.save(postEntity);
        var resDto = blogPostMapper.toDto(savedEntity);
        return Result.ok(resDto);
    }

    @WrapResult
    @Transactional
    public Result<ReadBlogPostDto> updatePost(Long id, UpdateBlogPostDto dto){
        log.info("Updating blog post with id: {}", id);
        var post = postRepository.findById(id);
        if(post.isEmpty()){
            log.warn("Blog post with id {} not found", id);
            return Result.fail("Blog post not found");
        }

        var postEntity = post.get();
        blogPostMapper.updateEntity(postEntity, dto);
        var savedEntity = postRepository.save(postEntity);
        var resDto = blogPostMapper.toDto(savedEntity);
        return Result.ok(resDto);
    }

    @WrapResult
    @Transactional
    public Result<Void> deletePost(Long id){
        log.info("Deleting blog post with id: {}", id);
        var post = postRepository.findById(id);
        if(post.isEmpty()){
            log.warn("Blog post with id {} not found", id);
            return Result.fail("Blog post not found");
        }

        postRepository.delete(post.get());
        log.info("Blog post with id {} deleted", id);
        return Result.ok(null);
    }
}
