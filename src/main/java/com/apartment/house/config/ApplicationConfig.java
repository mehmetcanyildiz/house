package com.apartment.house.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig
{
    @Value("${secret.key}")
    public String secretKey;

    @Value("${jwt.expiration}")
    public Integer jwtExpiration;

    @Value("${mail.from}")
    public String mailFrom;

    @Value("${base.url}")
    public String baseUrl;

}
