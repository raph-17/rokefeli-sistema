package com.rokefeli.colmenares.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication

@EnableJpaRepositories(basePackages = "com.rokofeli.colmenares.api.repository")

public class ColmenaresBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ColmenaresBackendApplication.class, args);

        System.out.println("🚀 Servidor ROKEFELI iniciado.");
	}

}