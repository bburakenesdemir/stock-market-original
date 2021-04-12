package com.burakenesdemir.stockmarket.security.api;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class ApiJWTAuthorizationFilter extends BasicAuthenticationFilter {
    public ApiJWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }
}