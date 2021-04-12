package com.burakenesdemir.stockmarket.service;

import com.burakenesdemir.stockmarket.data.User;
import com.burakenesdemir.stockmarket.data.UserRepository;
import com.burakenesdemir.stockmarket.dto.TokenDto;
import com.burakenesdemir.stockmarket.exception.NoDataFoundError;
import com.burakenesdemir.stockmarket.security.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.burakenesdemir.stockmarket.util.ErrorUtil.PASSWORD_VALIDATION_ERROR;
import static com.burakenesdemir.stockmarket.util.ErrorUtil.USER_NOT_FOUND_ERROR;

@Service
public class AuthenticationService {

    @Autowired
    UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    PasswordEncoder passwordEncoder;

    public TokenDto login(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            LOGGER.error("{} : {} ", AuthenticationService.class.getName(), USER_NOT_FOUND_ERROR);
            throw new NoDataFoundError(USER_NOT_FOUND_ERROR);
        } else if (user.getPassword() == null || !passwordEncoder.matches(password, user.getPassword())) {
            LOGGER.error("{} : {} ", AuthenticationService.class.getName(), PASSWORD_VALIDATION_ERROR);
            throw new NoDataFoundError(PASSWORD_VALIDATION_ERROR);
        }

        return new TokenDto(getJWTToken(user));
    }

    private String getJWTToken(User user) {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");
        return Jwts
                .builder()
                .setId(Long.toString(user.getId()))
                .setSubject(user.getUsername())
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .claim("username", user.getUsername())
                .claim("email", user.getUserEmail())
                .claim("name", user.getName())
                .claim("surname", user.getSurname())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60000000))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET).compact();
    }
}