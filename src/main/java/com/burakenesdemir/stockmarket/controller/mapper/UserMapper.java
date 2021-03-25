package com.burakenesdemir.stockmarket.controller.mapper;

import com.burakenesdemir.stockmarket.base.mapper.Converter;
import com.burakenesdemir.stockmarket.data.User;
import com.burakenesdemir.stockmarket.dto.UserDto;
import com.burakenesdemir.stockmarket.resource.UserResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends Converter<UserDto, User, UserResource> {
    User toEntity(UserDto userDto);

    UserResource toResource(User user);
}