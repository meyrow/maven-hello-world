package com.myapp;

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

    private static final String VERSION = "1.0.0";
    private static final String AUTHOR = "Ilan";
    private final LocalDateTime startTime = LocalDateTime.now();

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        System.out.println("🚀 Application started successfully!");
        System.out.println("📡 Server running on http://localhost:8080");
        System.out.println("🌐 Dashboard available at http://localhost:8080");
        System.out.println("👽 Ready for extraction from Earth!");
    }

    @RestController
    @RequestMapping("/api")
    public class ApiController {

        @GetMapping("/greeting")
        public Map<String, Object> greeting() {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Hello World from " + AUTHOR + "! 👽");
            response.put("status", "Spaceship signal transmitted successfully");
            response.put("version", VERSION);
            response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            return response;
        }

        @GetMapping("/health")
        public Map<String, Object> apiHealth() {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "UP");
            response.put("application", "Maven Hello World Ilan 2");
            response.put("version", VERSION);
            response.put("uptime", getUptime());
            return response;
        }

        @GetMapping("/info")
        public Map<String, Object> info() {
            Map<String, Object> response = new HashMap<>();
            response.put("application", "Maven Hello World - DevOps Exercise");
            response.put("version", VERSION);
            response.put("author", AUTHOR);
            response.put("description", "Production-ready Spring Boot REST API");
            response.put("mission", "Signal spaceship from Kepler-186f for extraction");
            response.put("startTime", startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            response.put("uptime", getUptime());

            Map<String, String> endpoints = new HashMap<>();
            endpoints.put("/", "React Dashboard (UI)");
            endpoints.put("/api/greeting", "API greeting endpoint");
            endpoints.put("/health", "Health check endpoint");
            endpoints.put("/api/health", "API health endpoint");
            endpoints.put("/api/info", "Application information");
            response.put("endpoints", endpoints);

            return response;
        }
    }

    // Keep /health at root level for Kubernetes probes
    @RestController
    public class HealthController {

        @GetMapping("/health")
        public Map<String, Object> health() {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "UP");
            response.put("application", "Maven Hello World Ilan");
            response.put("version", VERSION);
            response.put("uptime", getUptime());
            return response;
        }
    }

    @Configuration
    public class WebConfig implements WebMvcConfigurer {
        @Override
        public void addViewControllers(ViewControllerRegistry registry) {
            // Forward root to index.html
            registry.addViewController("/").setViewName("forward:/index.html");
        }
    }

    private String getUptime() {
        long seconds = java.time.Duration.between(startTime, LocalDateTime.now()).getSeconds();
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }
}