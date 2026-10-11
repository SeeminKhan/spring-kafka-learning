package com.example.orderprocessing.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration for CORS (Cross-Origin Resource Sharing).
 * 
 * Allows the React frontend running on localhost:5173 (Vite default port)
 * to make requests to this Spring Boot backend on localhost:8080.
 * 
 * Without CORS configuration, browsers would block the frontend from
 * accessing the backend due to same-origin policy.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")  // Apply to all /api/* endpoints
                .allowedOrigins(allowedOrigins.split(","))  // Allow React dev servers
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);  // Cache preflight response for 1 hour
    }
}
