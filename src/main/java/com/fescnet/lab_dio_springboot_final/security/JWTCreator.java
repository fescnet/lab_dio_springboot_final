package com.fescnet.lab_dio_springboot_final.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.lang.NonNull;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

/**
 * Responsible for storing and retrieving user data from JWT
 */
public class JWTCreator {
    public static final String HEADER_AUTHORIZATION = "Authorization";

    private static Key getSigningKey(String key) {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Creates a JWT
     * @param key key to sign the token
     * @param id userId to store on the token
     * @param expiration token's TTL
     * @param prefix prefix used along the JWT on the "Authorization" HTTP header
     * @return String JWT
     */
    public static String create(@NonNull String key, @NonNull Long id, @NonNull Long expiration, @NonNull String prefix) {

        String jwt = Jwts.builder()
                .subject(id.toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(key))
                .compact();
        return prefix.concat(" ").concat(jwt);
    }

    /**
     * Check if a token is valid and extract the userID from it
     * @param token JWT
     * @param key the same key used to sign the token at the creation
     * @param prefix prefix used along the JWT on the "Authorization" HTTP header
     * @return userID
     */
    public static Long extract(String token, String key, String prefix)
            throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException {

        String onlyToken = token.replace(prefix.concat(" "), "");
        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) getSigningKey(key))
                .build()
                .parseSignedClaims(onlyToken)
                .getPayload();
        return Long.parseLong(claims.getSubject());
    }
}
