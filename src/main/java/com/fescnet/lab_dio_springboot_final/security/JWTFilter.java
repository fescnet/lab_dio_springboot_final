package com.fescnet.lab_dio_springboot_final.security;

import com.fescnet.lab_dio_springboot_final.controller.dto.ExceptionResponseDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
/**
 * Intercepts a request to check if the JWT is valid
 * If the JWT is valid, the user information is placed at the SecurityContextHolder context
 */
public class JWTFilter extends OncePerRequestFilter {

    private final SecurityConfig securityConfig;

    @Autowired
    public JWTFilter(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader(JWTCreator.HEADER_AUTHORIZATION);
        try {
            if (token != null && !token.isEmpty()) {
                Long id = JWTCreator.extract(token, securityConfig.getKey(), securityConfig.getPrefix());

                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(("client")));
                UsernamePasswordAuthenticationToken userToken =
                        new UsernamePasswordAuthenticationToken(
                                id,
                                null,
                                authorities);

                SecurityContextHolder.getContext().setAuthentication(userToken);

            } else {
                SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            handleException(response, e, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            handleException(response, e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handle exception thrown by the JWTFilter
     * @param response HttpServletResponse object
     * @param e Exception thrown
     * @param status Response HTTP status
     */
    private void handleException(HttpServletResponse response, Exception e, HttpStatus status) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(new ExceptionResponseDto(e.getMessage()).toJson());
    }
}
