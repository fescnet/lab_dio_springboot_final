package com.fescnet.lab_dio_springboot_final.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Provides information about the authenticated user
 */
public class AuthenticatedUser {
    public static Long getId(){
        UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return (Long) userDetails.getPrincipal();
    }
}
