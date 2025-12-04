package com.vladdjuga.blogsite.mapper.blog_post;

import com.vladdjuga.blogsite.dto.blog_post.CreateBlogPostDto;
import com.vladdjuga.blogsite.dto.blog_post.ReadBlogPostDto;
import com.vladdjuga.blogsite.dto.blog_post.UpdateBlogPostDto;
import com.vladdjuga.blogsite.model.entity.BlogPostEntity;
import com.vladdjuga.blogsite.model.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class BlogPostMapper {
    public ReadBlogPostDto toDto(BlogPostEntity blogPost){
        if(blogPost == null) return null;
        return new ReadBlogPostDto(
                blogPost.getId(),
                blogPost.getTitle(),
                blogPost.getContent(),
                blogPost.getAuthor().getId(),
                blogPost.getCreatedAt()
        );
    }
    public BlogPostEntity toEntity(CreateBlogPostDto blogPostDto, UserEntity author){
        if(blogPostDto == null || author == null) return null;
        BlogPostEntity blogPost = new BlogPostEntity();
        blogPost.setTitle(blogPostDto.title());
        blogPost.setContent(blogPostDto.content());
        blogPost.setAuthor(author);
        return blogPost;
    }

    public void updateEntity(BlogPostEntity entity, UpdateBlogPostDto dto){
        if(dto.title() != null){
            entity.setTitle(dto.title());
        }
        if(dto.content() != null){
            entity.setContent(dto.content());
        }
    }
}
