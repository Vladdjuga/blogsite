package com.vladdjuga.blogsite.service;

import com.vladdjuga.blogsite.dto.blog_post.CreateBlogPostDto;
import com.vladdjuga.blogsite.dto.blog_post.ReadBlogPostDto;
import com.vladdjuga.blogsite.dto.blog_post.UpdateBlogPostDto;
import com.vladdjuga.blogsite.mapper.blog_post.BlogPostMapper;
import com.vladdjuga.blogsite.model.entity.BlogPostEntity;
import com.vladdjuga.blogsite.model.entity.UserEntity;
import com.vladdjuga.blogsite.repository.BlogPostRepository;
import com.vladdjuga.blogsite.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlogPostServiceTest {

    @Mock
    private BlogPostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BlogPostMapper blogPostMapper;

    @InjectMocks
    private BlogPostService blogPostService;

    private UserEntity author;
    private BlogPostEntity postEntity;
    private ReadBlogPostDto readPostDto;

    @BeforeEach
    void setUp() {
        author = new UserEntity();
        author.setId(1L);
        author.setUsername("author");

        postEntity = new BlogPostEntity();
        postEntity.setId(10L);
        postEntity.setTitle("Test Post");
        postEntity.setContent("Test Content");
        postEntity.setAuthor(author);
        postEntity.setCreatedAt(new Date());

        readPostDto = new ReadBlogPostDto(10L, "Test Post", "Test Content", 1L, postEntity.getCreatedAt());
    }

    @Test
    void getAll_shouldReturnAllPosts() {
        when(postRepository.findAll()).thenReturn(List.of(postEntity));
        when(blogPostMapper.toDto(postEntity)).thenReturn(readPostDto);

        var result = blogPostService.getAll();

        assertTrue(result.isSuccess);
        assertEquals(1, result.value.size());
        assertEquals(readPostDto, result.value.getFirst());
        verify(postRepository).findAll();
    }

    @Test
    void getAll_shouldReturnEmptyListWhenNoPosts() {
        when(postRepository.findAll()).thenReturn(List.of());

        var result = blogPostService.getAll();

        assertTrue(result.isSuccess);
        assertTrue(result.value.isEmpty());
    }

    @Test
    void getById_shouldReturnPostWhenExists() {
        when(postRepository.findById(10L)).thenReturn(Optional.of(postEntity));
        when(blogPostMapper.toDto(postEntity)).thenReturn(readPostDto);

        var result = blogPostService.getById(10L);

        assertTrue(result.isSuccess);
        assertEquals(readPostDto, result.value);
        verify(postRepository).findById(10L);
    }

    @Test
    void getById_shouldFailWhenPostNotFound() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        var result = blogPostService.getById(99L);

        assertFalse(result.isSuccess);
        assertEquals("Blog post not found", result.error.message);
    }

    @Test
    void savePost_shouldSaveAndReturnPost() {
        CreateBlogPostDto createDto = new CreateBlogPostDto("New Post", "New Content", 1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(author));
        when(blogPostMapper.toEntity(createDto, author)).thenReturn(postEntity);
        when(postRepository.save(postEntity)).thenReturn(postEntity);
        when(blogPostMapper.toDto(postEntity)).thenReturn(readPostDto);

        var result = blogPostService.savePost(createDto);

        assertTrue(result.isSuccess);
        assertNotNull(result.value);
        verify(postRepository).save(postEntity);
    }

    @Test
    void savePost_shouldFailWhenPostIsNull() {
        var result = blogPostService.savePost(null);

        assertFalse(result.isSuccess);
        assertEquals("Post cannot be null", result.error.message);
        verify(postRepository, never()).save(any());
    }

    @Test
    void savePost_shouldFailWhenAuthorNotFound() {
        CreateBlogPostDto createDto = new CreateBlogPostDto("New Post", "New Content", 99L);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        var result = blogPostService.savePost(createDto);

        assertFalse(result.isSuccess);
        assertEquals("Author not found", result.error.message);
        verify(postRepository, never()).save(any());
    }

    @Test
    void updatePost_shouldUpdateAndReturnPost() {
        UpdateBlogPostDto updateDto = new UpdateBlogPostDto("Updated Title", "Updated Content");

        when(postRepository.findById(10L)).thenReturn(Optional.of(postEntity));
        when(postRepository.save(postEntity)).thenReturn(postEntity);
        when(blogPostMapper.toDto(postEntity)).thenReturn(readPostDto);

        var result = blogPostService.updatePost(10L, updateDto);

        assertTrue(result.isSuccess);
        assertNotNull(result.value);
        verify(blogPostMapper).updateEntity(postEntity, updateDto);
        verify(postRepository).save(postEntity);
    }

    @Test
    void updatePost_shouldFailWhenPostNotFound() {
        UpdateBlogPostDto updateDto = new UpdateBlogPostDto("Updated Title", null);

        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        var result = blogPostService.updatePost(99L, updateDto);

        assertFalse(result.isSuccess);
        assertEquals("Blog post not found", result.error.message);
        verify(postRepository, never()).save(any());
    }

    @Test
    void deletePost_shouldDeletePost() {
        when(postRepository.findById(10L)).thenReturn(Optional.of(postEntity));

        var result = blogPostService.deletePost(10L);

        assertTrue(result.isSuccess);
        verify(postRepository).delete(postEntity);
    }

    @Test
    void deletePost_shouldFailWhenPostNotFound() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        var result = blogPostService.deletePost(99L);

        assertFalse(result.isSuccess);
        assertEquals("Blog post not found", result.error.message);
        verify(postRepository, never()).delete(any());
    }
}

