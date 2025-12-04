package com.vladdjuga.blogsite.mapper.user;

import com.vladdjuga.blogsite.dto.user.ReadUserDto;
import com.vladdjuga.blogsite.dto.user.RegisterUserDto;
import com.vladdjuga.blogsite.dto.user.UpdateUserDto;
import com.vladdjuga.blogsite.model.Role;
import com.vladdjuga.blogsite.model.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    void toDto_shouldMapEntityToDto() {
        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setUsername("testuser");
        entity.setEmail("test@example.com");
        entity.setRole(Role.USER);
        entity.setCreatedAt(new Date());

        ReadUserDto dto = userMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getUsername(), dto.username());
        assertEquals(entity.getEmail(), dto.email());
        assertEquals(entity.getRole(), dto.role());
        assertEquals(entity.getCreatedAt(), dto.createdAt());
    }

    @Test
    void toDto_shouldReturnNullWhenEntityIsNull() {
        ReadUserDto dto = userMapper.toDto(null);

        assertNull(dto);
    }

    @Test
    void toEntity_shouldMapDtoToEntity() {
        RegisterUserDto dto = new RegisterUserDto("testuser", "test@example.com", "password123");

        UserEntity entity = userMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.username(), entity.getUsername());
        assertEquals(dto.email(), entity.getEmail());
        assertEquals(dto.password(), entity.getPassword());
    }

    @Test
    void toEntity_shouldReturnNullWhenDtoIsNull() {
        UserEntity entity = userMapper.toEntity(null);

        assertNull(entity);
    }

    @Test
    void updateEntity_shouldUpdateOnlyNonNullFields() {
        UserEntity entity = new UserEntity();
        entity.setUsername("oldUsername");
        entity.setEmail("old@example.com");

        UpdateUserDto dto = new UpdateUserDto("newUsername", null);

        userMapper.updateEntity(entity, dto);

        assertEquals("newUsername", entity.getUsername());
        assertEquals("old@example.com", entity.getEmail());
    }

    @Test
    void updateEntity_shouldUpdateAllFieldsWhenProvided() {
        UserEntity entity = new UserEntity();
        entity.setUsername("oldUsername");
        entity.setEmail("old@example.com");

        UpdateUserDto dto = new UpdateUserDto("newUsername", "new@example.com");

        userMapper.updateEntity(entity, dto);

        assertEquals("newUsername", entity.getUsername());
        assertEquals("new@example.com", entity.getEmail());
    }
}

