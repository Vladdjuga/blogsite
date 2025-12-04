package com.vladdjuga.blogsite.security;

import com.vladdjuga.blogsite.repository.BlogPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("securityService")
@RequiredArgsConstructor
public class SecurityService {

    private final BlogPostRepository blogPostRepository;

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        return auth.getName();
    }

    public boolean isPostOwner(Long postId) {
        String username = getCurrentUsername();
        if (username == null) {
            return false;
        }
        return blogPostRepository.findById(postId)
                .map(post -> post.getAuthor().getUsername().equals(username))
                .orElse(false);
    }
}

