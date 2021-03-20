package org.libraryservice.mapper;

import org.libraryservice.dto.UserDto;
import org.libraryservice.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper implements EntityDtoMapper<User, UserDto>{
    public UserDto toDto(User user){
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public User toEntity (UserDto user){
        return User.builder()
                .name(user.getName())
                .build();
    }
}
