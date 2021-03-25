package com.burakenesdemir.stockmarket.data;

import com.burakenesdemir.stockmarket.base.data.entity.BaseEntity;
import com.burakenesdemir.stockmarket.type.UserStatus;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "user")
public class User extends BaseEntity {

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String activationCode;

    private String passwordActivationCode;

    private Date lastLoginDate;

    private String name;

    private String surname;

    private String cellphone;

    private String userEmail;

    @Enumerated(EnumType.STRING)
    public UserStatus userStatus = UserStatus.NEW;
}