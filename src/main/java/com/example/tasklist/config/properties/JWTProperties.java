package com.example.tasklist.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("security.jwt")
public class JWTProperties {

    private String secret;
    private long access;
    private long refresh;

}
