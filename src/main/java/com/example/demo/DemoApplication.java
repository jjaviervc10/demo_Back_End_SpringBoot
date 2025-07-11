package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
    public CommandLineRunner printEndpoints(RequestMappingHandlerMapping mapping) {
        return args -> {
            System.out.println("=== ENDPOINTS REGISTRADOS ===");
            mapping.getHandlerMethods().forEach((key, value) -> {
                System.out.println(key + " -> " + value);
            });
        };
    }
}
