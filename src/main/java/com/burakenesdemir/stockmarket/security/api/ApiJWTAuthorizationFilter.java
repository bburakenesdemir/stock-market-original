package com.burakenesdemir.stockmarket.security.api;

import com.burakenesdemir.stockmarket.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;

public class ApiJWTAuthorizationFilter extends BasicAuthenticationFilter {

    public ApiJWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String authorizationHeader = req.getHeader(SecurityConstants.HEADER_AUTHORIZATION);
        if (authorizationHeader == null || !(authorizationHeader.startsWith(SecurityConstants.TOKEN_PREFIX)
                || authorizationHeader.startsWith(SecurityConstants.BASIC_AUTH_PREFIX))) {
            chain.doFilter(req, res);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.HEADER_AUTHORIZATION);
        if (token != null) {
            if (token.contains(SecurityConstants.BASIC_AUTH_PREFIX)) {
                token = token.split(" ")[1];
                byte[] decoded = Base64.getDecoder().decode(token.getBytes(StandardCharsets.UTF_8));
                String[] credentialsArray = new String(decoded, StandardCharsets.UTF_8).split(":", 2);
                String username = credentialsArray[0];
                String password = credentialsArray[1];
            } else {
                Claims claims = Jwts.parser()
                        .setSigningKey(SecurityConstants.SECRET)
                        .parseClaimsJws(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
                        .getBody();
                String user = claims.getSubject();
                if (user != null) {
                    return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                }
            }
            return null;
        }
        return null;
    }
}