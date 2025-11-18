package com.vladdjuga.blogsite.service;

import com.vladdjuga.blogsite.model.entity.UserEntity;
import com.vladdjuga.blogsite.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> getAll(){
        log.info("Getting all users");
        return userRepository.findAll();
    }

    @Transactional
    public void saveUser(UserEntity user){
        log.info("Saving user");
        log.info("User: {}", user);
        userRepository.save(user);
    }
}
