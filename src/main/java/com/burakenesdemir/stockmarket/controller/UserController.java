package com.burakenesdemir.stockmarket.controller;

import com.burakenesdemir.stockmarket.controller.mapper.UserMapper;
import com.burakenesdemir.stockmarket.dto.UserDto;
import com.burakenesdemir.stockmarket.resource.UserResource;
import com.burakenesdemir.stockmarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @PostMapping(value = "/register")
    public UserResource register(@RequestBody UserDto userDto) {
        return userMapper.toResource(userService.register(userDto));
    }

    @PostMapping(value = "/email-activation/code/{code}")
    public ResponseEntity<String> activationControl(@PathVariable(name = "code") String code) {
        userService.activationControl(code);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/reset-password")
    public void resetPassword(@RequestParam(name = "email") String email) {
        userService.resetPassword(email);
    }


    @PostMapping(value = "/change-password")
    public void changePassword(@RequestParam(name = "key") String key, @RequestParam(value = "password") String password) {
        userService.changePassword(key, password);
    }

}