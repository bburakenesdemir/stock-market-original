package com.burakenesdemir.stockmarket.controller;

import com.burakenesdemir.stockmarket.dto.TokenDto;
import com.burakenesdemir.stockmarket.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping(value = "/login")
    public TokenDto login(@RequestParam(value = "username") String username,
                          @RequestParam(value = "password") String password) {
        return authenticationService.login(username, password);
    }
}
