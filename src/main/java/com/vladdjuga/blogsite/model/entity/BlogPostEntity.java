package com.vladdjuga.blogsite.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Data
@Table(name = "blog_posts")
public class BlogPostEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String content;

    private String author;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp(source = SourceType.DB)
    private Date createdAt;
}
