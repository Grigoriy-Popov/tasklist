package com.example.tasklist.web.mappers;

import com.example.tasklist.model.user.User;
import com.example.tasklist.web.dto.user.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDto> {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "passwordConfirmation", ignore = true)
    UserDto toDto(User user);

}
