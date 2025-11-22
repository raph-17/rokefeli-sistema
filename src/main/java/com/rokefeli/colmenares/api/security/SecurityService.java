package com.rokefeli.colmenares.api.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {

    public boolean isSelf(Authentication auth, Long id) {
        JwtUserDetails user = (JwtUserDetails) auth.getPrincipal();
        return user.getId().equals(id);
    }
}
