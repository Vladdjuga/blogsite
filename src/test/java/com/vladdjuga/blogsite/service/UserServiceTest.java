package com.vladdjuga.blogsite.service;

import com.vladdjuga.blogsite.dto.user.ReadUserDto;
import com.vladdjuga.blogsite.dto.user.RegisterUserDto;
import com.vladdjuga.blogsite.dto.user.UpdateUserDto;
import com.vladdjuga.blogsite.mapper.user.UserMapper;
import com.vladdjuga.blogsite.model.Role;
import com.vladdjuga.blogsite.model.entity.UserEntity;
import com.vladdjuga.blogsite.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserEntity userEntity;
    private ReadUserDto readUserDto;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("testuser");
        userEntity.setEmail("test@example.com");
        userEntity.setPassword("encodedPassword");
        userEntity.setRole(Role.USER);
        userEntity.setCreatedAt(new Date());

        readUserDto = new ReadUserDto(1L, "testuser", "test@example.com", Role.USER, userEntity.getCreatedAt());
    }

    @Test
    void getAll_shouldReturnAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(userEntity));
        when(userMapper.toDto(userEntity)).thenReturn(readUserDto);

        var result = userService.getAll();

        assertTrue(result.isSuccess);
        assertEquals(1, result.value.size());
        assertEquals(readUserDto, result.value.getFirst());
        verify(userRepository).findAll();
    }

    @Test
    void getAll_shouldReturnEmptyListWhenNoUsers() {
        when(userRepository.findAll()).thenReturn(List.of());

        var result = userService.getAll();

        assertTrue(result.isSuccess);
        assertTrue(result.value.isEmpty());
    }

    @Test
    void getById_shouldReturnUserWhenExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userMapper.toDto(userEntity)).thenReturn(readUserDto);

        var result = userService.getById(1L);

        assertTrue(result.isSuccess);
        assertEquals(readUserDto, result.value);
        verify(userRepository).findById(1L);
    }

    @Test
    void getById_shouldFailWhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        var result = userService.getById(99L);

        assertFalse(result.isSuccess);
        assertEquals("User not found", result.error.message);
    }

    @Test
    void saveUser_shouldSaveAndReturnUser() {
        RegisterUserDto registerDto = new RegisterUserDto("newuser", "new@example.com", "password123");
        UserEntity newEntity = new UserEntity();
        newEntity.setUsername("newuser");
        newEntity.setEmail("new@example.com");
        newEntity.setPassword("password123");

        when(userMapper.toEntity(registerDto)).thenReturn(newEntity);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userMapper.toDto(userEntity)).thenReturn(readUserDto);

        var result = userService.saveUser(registerDto);

        assertTrue(result.isSuccess);
        assertNotNull(result.value);
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void updateUser_shouldUpdateAndReturnUser() {
        UpdateUserDto updateDto = new UpdateUserDto("updateduser", "updated@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toDto(userEntity)).thenReturn(readUserDto);

        var result = userService.updateUser(1L, updateDto);

        assertTrue(result.isSuccess);
        assertNotNull(result.value);
        verify(userMapper).updateEntity(userEntity, updateDto);
        verify(userRepository).save(userEntity);
    }

    @Test
    void updateUser_shouldFailWhenUserNotFound() {
        UpdateUserDto updateDto = new UpdateUserDto("updateduser", null);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        var result = userService.updateUser(99L, updateDto);

        assertFalse(result.isSuccess);
        assertEquals("User not found", result.error.message);
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_shouldDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        var result = userService.deleteUser(1L);

        assertTrue(result.isSuccess);
        verify(userRepository).delete(userEntity);
    }

    @Test
    void deleteUser_shouldFailWhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        var result = userService.deleteUser(99L);

        assertFalse(result.isSuccess);
        assertEquals("User not found", result.error.message);
        verify(userRepository, never()).delete(any());
    }
}
