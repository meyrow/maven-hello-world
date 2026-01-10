package com.myapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class App {

    private static final String AUTHOR = "Ilan";

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        System.out.println("🚀 Application started successfully!");
        System.out.println("📡 Server running on http://localhost:8080");
        System.out.println("🌐 Dashboard available at http://localhost:8080");
    }

    @RestController
    @RequestMapping("/api")
    public static class ApiController {

        @Value("${app.version}")
        private String version;

        private final LocalDateTime startTime = LocalDateTime.now();

        @GetMapping("/greeting")
        public Map<String, Object> greeting() {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Hello World from " + AUTHOR + "!");
            response.put("status", "Service operational");
            response.put("version", version);
            response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            return response;
        }

        @GetMapping("/info")
        public Map<String, Object> info() {
            Map<String, Object> response = new HashMap<>();
            response.put("application", "Maven Hello World - DevOps Exercise");
            response.put("version", version);
            response.put("author", AUTHOR);
            response.put("description", "Production-ready Spring Boot REST API");
            response.put("startTime", startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            response.put("uptime", getUptime(startTime));

            Map<String, String> endpoints = new HashMap<>();
            endpoints.put("/", "React Dashboard (UI)");
            endpoints.put("/api/greeting", "API greeting endpoint");
            endpoints.put("/health", "Health check endpoint");
            endpoints.put("/api/info", "Application information");
            response.put("endpoints", endpoints);

            return response;
        }

        private String getUptime(LocalDateTime start) {
            long seconds = java.time.Duration.between(start, LocalDateTime.now()).getSeconds();
            long hours = seconds / 3600;
            long minutes = (seconds % 3600) / 60;
            long secs = seconds % 60;
            return String.format("%02d:%02d:%02d", hours, minutes, secs);
        }
    }

    @RestController
    public static class HealthController {

        @Value("${app.version}")
        private String version;

        private final LocalDateTime startTime = LocalDateTime.now();

        @GetMapping("/health")
        public Map<String, Object> health() {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "UP");
            response.put("application", "Maven Hello World");
            response.put("version", version);
            response.put("uptime", getUptime(startTime));
            return response;
        }

        private String getUptime(LocalDateTime start) {
            long seconds = java.time.Duration.between(start, LocalDateTime.now()).getSeconds();
            long hours = seconds / 3600;
            long minutes = (seconds % 3600) / 60;
            long secs = seconds % 60;
            return String.format("%02d:%02d:%02d", hours, minutes, secs);
        }
    }

    @Configuration
    public static class WebConfig implements WebMvcConfigurer {
        @Override
        public void addViewControllers(ViewControllerRegistry registry) {
            registry.addViewController("/").setViewName("forward:/index.html");
        }
    }
}