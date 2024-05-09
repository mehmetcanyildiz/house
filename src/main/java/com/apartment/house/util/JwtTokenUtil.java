package com.apartment.house.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class JwtTokenUtil {

    private static final String SECRET_KEY = "IpJm1MQuUG6wufBnpxeqRhWZCzHAzRfQ";
    private static final long EXPIRATION_TIME = 86400000;


    public String generateToken(String value) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder().setSubject(value).setIssuedAt(now).setExpiration(expiryDate).signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }
}