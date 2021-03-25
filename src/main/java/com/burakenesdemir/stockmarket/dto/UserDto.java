package com.burakenesdemir.stockmarket.dto;

import com.burakenesdemir.stockmarket.base.dto.AbstractDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class UserDto extends AbstractDto {
    private String username;

    private String password;

    private String confirmPassword;

    private String name;

    private String surname;

    private String cellphone;

    private String userEmail;
}