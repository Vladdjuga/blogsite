package com.vladdjuga.blogsite.mapper.blog_post;

import com.vladdjuga.blogsite.dto.blog_post.CreateBlogPostDto;
import com.vladdjuga.blogsite.dto.blog_post.ReadBlogPostDto;
import com.vladdjuga.blogsite.dto.blog_post.UpdateBlogPostDto;
import com.vladdjuga.blogsite.model.entity.BlogPostEntity;
import com.vladdjuga.blogsite.model.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class BlogPostMapperTest {

    private BlogPostMapper blogPostMapper;

    @BeforeEach
    void setUp() {
        blogPostMapper = new BlogPostMapper();
    }

    @Test
    void toDto_shouldMapEntityToDto() {
        UserEntity author = new UserEntity();
        author.setId(1L);

        BlogPostEntity entity = new BlogPostEntity();
        entity.setId(10L);
        entity.setTitle("Test Title");
        entity.setContent("Test Content");
        entity.setAuthor(author);
        entity.setCreatedAt(new Date());

        ReadBlogPostDto dto = blogPostMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getTitle(), dto.title());
        assertEquals(entity.getContent(), dto.content());
        assertEquals(author.getId(), dto.authorId());
        assertEquals(entity.getCreatedAt(), dto.createdAt());
    }

    @Test
    void toDto_shouldReturnNullWhenEntityIsNull() {
        ReadBlogPostDto dto = blogPostMapper.toDto(null);

        assertNull(dto);
    }

    @Test
    void toEntity_shouldMapDtoToEntity() {
        UserEntity author = new UserEntity();
        author.setId(1L);
        author.setUsername("testuser");

        CreateBlogPostDto dto = new CreateBlogPostDto("Test Title", "Test Content");

        BlogPostEntity entity = blogPostMapper.toEntity(dto, author);

        assertNotNull(entity);
        assertEquals(dto.title(), entity.getTitle());
        assertEquals(dto.content(), entity.getContent());
        assertEquals(author, entity.getAuthor());
    }

    @Test
    void toEntity_shouldReturnNullWhenDtoIsNull() {
        UserEntity author = new UserEntity();

        BlogPostEntity entity = blogPostMapper.toEntity(null, author);

        assertNull(entity);
    }

    @Test
    void toEntity_shouldReturnNullWhenAuthorIsNull() {
        CreateBlogPostDto dto = new CreateBlogPostDto("Title", "Content");

        BlogPostEntity entity = blogPostMapper.toEntity(dto, null);

        assertNull(entity);
    }

    @Test
    void updateEntity_shouldUpdateOnlyNonNullFields() {
        BlogPostEntity entity = new BlogPostEntity();
        entity.setTitle("Old Title");
        entity.setContent("Old Content");

        UpdateBlogPostDto dto = new UpdateBlogPostDto("New Title", null);

        blogPostMapper.updateEntity(entity, dto);

        assertEquals("New Title", entity.getTitle());
        assertEquals("Old Content", entity.getContent());
    }

    @Test
    void updateEntity_shouldUpdateAllFieldsWhenProvided() {
        BlogPostEntity entity = new BlogPostEntity();
        entity.setTitle("Old Title");
        entity.setContent("Old Content");

        UpdateBlogPostDto dto = new UpdateBlogPostDto("New Title", "New Content");

        blogPostMapper.updateEntity(entity, dto);

        assertEquals("New Title", entity.getTitle());
        assertEquals("New Content", entity.getContent());
    }
}

