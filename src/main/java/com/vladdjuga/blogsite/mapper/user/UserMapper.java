package com.vladdjuga.blogsite.mapper.user;

import com.vladdjuga.blogsite.dto.user.RegisterUserDto;
import com.vladdjuga.blogsite.dto.user.ReadUserDto;
import com.vladdjuga.blogsite.dto.user.UpdateUserDto;
import com.vladdjuga.blogsite.model.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public ReadUserDto toDto(UserEntity user){
        if(user == null) return null;
        return new ReadUserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
    public UserEntity toEntity(RegisterUserDto userDto){
        if(userDto == null) return null;
        UserEntity user = new UserEntity();
        user.setUsername(userDto.username());
        user.setEmail(userDto.email());
        user.setPassword(userDto.password());
        return user;
    }

    public void updateEntity(UserEntity entity, UpdateUserDto dto){
        if(dto.username() != null){
            entity.setUsername(dto.username());
        }
        if(dto.email() != null){
            entity.setEmail(dto.email());
        }
    }
}
