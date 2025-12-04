package com.vladdjuga.blogsite.service;

import com.vladdjuga.blogsite.annotation.WrapResult;
import com.vladdjuga.blogsite.dto.user.RegisterUserDto;
import com.vladdjuga.blogsite.dto.user.ReadUserDto;
import com.vladdjuga.blogsite.dto.user.UpdateUserDto;
import com.vladdjuga.blogsite.mapper.user.UserMapper;
import com.vladdjuga.blogsite.model.Role;
import com.vladdjuga.blogsite.repository.UserRepository;
import com.vladdjuga.blogsite.result.Result;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @WrapResult
    public Result<List<ReadUserDto>> getAll(){
        log.info("Getting all users");
        var users = userRepository.findAll();
        var res = users.stream().map(userMapper::toDto).toList();
        return Result.ok(res);
    }

    @WrapResult
    public Result<ReadUserDto> getById(Long id){
        log.info("Getting user by id: {}", id);
        var user = userRepository.findById(id);
        if(user.isEmpty()){
            log.warn("User with id {} not found", id);
            return Result.fail("User not found");
        }
        return Result.ok(userMapper.toDto(user.get()));
    }

    @WrapResult
    @Transactional
    public Result<ReadUserDto> saveUser(RegisterUserDto user){
        log.info("Saving user");
        log.info("User: {}", user);
        var userEntity = userMapper.toEntity(user);

        // Encode the password before saving
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        var savedEntity = userRepository.save(userEntity);
        var resDto = userMapper.toDto(savedEntity);
        return Result.ok(resDto);
    }

    @WrapResult
    @Transactional
    public Result<ReadUserDto> updateUser(Long id, UpdateUserDto dto){
        log.info("Updating user with id: {}", id);
        var user = userRepository.findById(id);
        if(user.isEmpty()){
            log.warn("User with id {} not found", id);
            return Result.fail("User not found");
        }

        var userEntity = user.get();
        userMapper.updateEntity(userEntity, dto);
        var savedEntity = userRepository.save(userEntity);
        var resDto = userMapper.toDto(savedEntity);
        return Result.ok(resDto);
    }

    @WrapResult
    @Transactional
    public Result<Void> deleteUser(Long id){
        log.info("Deleting user with id: {}", id);
        var user = userRepository.findById(id);
        if(user.isEmpty()){
            log.warn("User with id {} not found", id);
            return Result.fail("User not found");
        }

        userRepository.delete(user.get());
        log.info("User with id {} deleted", id);
        return Result.ok(null);
    }

    @WrapResult
    public Result<ReadUserDto> getByUsername(String username){
        log.info("Getting user by username: {}", username);
        var user = userRepository.findByUsername(username);
        if(user.isEmpty()){
            log.warn("User with username {} not found", username);
            return Result.fail("User not found");
        }
        return Result.ok(userMapper.toDto(user.get()));
    }

    @WrapResult
    @Transactional
    public Result<ReadUserDto> updateByUsername(String username, UpdateUserDto dto){
        log.info("Updating user with username: {}", username);
        var user = userRepository.findByUsername(username);
        if(user.isEmpty()){
            log.warn("User with username {} not found", username);
            return Result.fail("User not found");
        }

        var userEntity = user.get();
        userMapper.updateEntity(userEntity, dto);
        var savedEntity = userRepository.save(userEntity);
        var resDto = userMapper.toDto(savedEntity);
        return Result.ok(resDto);
    }

    @WrapResult
    @Transactional
    public Result<Void> deleteByUsername(String username){
        log.info("Deleting user with username: {}", username);
        var user = userRepository.findByUsername(username);
        if(user.isEmpty()){
            log.warn("User with username {} not found", username);
            return Result.fail("User not found");
        }

        userRepository.delete(user.get());
        log.info("User with username {} deleted", username);
        return Result.ok(null);
    }

    @WrapResult
    @Transactional
    public Result<ReadUserDto> changeRole(Long id, Role role) {
        log.info("Changing role for user with id: {} to {}", id, role);
        var user = userRepository.findById(id);
        if (user.isEmpty()) {
            log.warn("User with id {} not found", id);
            return Result.fail("User not found");
        }

        var userEntity = user.get();
        userEntity.setRole(role);
        var savedEntity = userRepository.save(userEntity);
        var resDto = userMapper.toDto(savedEntity);
        log.info("Role changed for user with id: {} to {}", id, role);
        return Result.ok(resDto);
    }
}
