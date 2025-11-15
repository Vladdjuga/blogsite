package com.vladdjuga.blogsite.repository;

import com.vladdjuga.blogsite.model.entity.BlogPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPostEntity, Long> {
}
