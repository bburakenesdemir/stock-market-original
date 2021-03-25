package com.burakenesdemir.stockmarket.resource;

import com.burakenesdemir.stockmarket.base.resource.AbstractResource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UserResource extends AbstractResource {
    private Long userId;

    private String username;

    private String name;

    private String surname;

    private String hashedPassword;
}