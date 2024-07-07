package com.example.tasklist.config;

import com.example.tasklist.web.security.expression.CustomSecurityExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final ApplicationContext applicationContext;

    @Bean
    public MethodSecurityExpressionHandler expressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new CustomSecurityExceptionHandler();
        expressionHandler.setApplicationContext(applicationContext);
        return expressionHandler;
    }
}
