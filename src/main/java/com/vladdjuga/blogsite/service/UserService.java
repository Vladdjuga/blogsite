package com.vladdjuga.blogsite.service;

import com.vladdjuga.blogsite.dto.user.CreateUserDto;
import com.vladdjuga.blogsite.dto.user.ReadUserDto;
import com.vladdjuga.blogsite.mapper.user.UserMapper;
import com.vladdjuga.blogsite.model.entity.UserEntity;
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
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Result<List<ReadUserDto>> getAll(){
        log.info("Getting all users");
        var users = userRepository.findAll();
        var res = users.stream().map(userMapper::toDto).toList();
        return Result.ok(res);
    }

    @Transactional
    public Result<ReadUserDto> saveUser(CreateUserDto user){
        log.info("Saving user");
        log.info("User: {}", user);
        var userEntity = userMapper.toEntity(user);
        var savedEntity = userRepository.save(userEntity);
        var resDto = userMapper.toDto(savedEntity);
        return Result.ok(resDto);
    }
}
