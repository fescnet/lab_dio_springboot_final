package com.fescnet.lab_dio_springboot_final.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security.config")
@Getter @Setter
/**
 * Exposes security configurations from application-{env}.yml to the application
 */
public class SecurityConfig {
    private String prefix;
    private String key;
    private Long expiration;
}
