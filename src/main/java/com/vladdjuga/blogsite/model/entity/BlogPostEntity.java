package com.vladdjuga.blogsite.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.util.Date;

@Entity
@Data
@Table(name = "blog_posts")
@SequenceGenerator(name = "blog_posts_seq", sequenceName = "blog_posts_seq", allocationSize = 1)
public class BlogPostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blog_posts_seq")
    private Long id;

    private String title;

    private String content;

    private String author;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp(source = SourceType.DB)
    private Date createdAt;
}
