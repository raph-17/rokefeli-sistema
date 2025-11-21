package com.rokefeli.colmenares.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
// 1. Fuerza la búsqueda de repositorios en este paquete específico
@EnableJpaRepositories(basePackages = "com.rokefeli.colmenares.api.repository")
// 2. Fuerza la búsqueda de entidades (Tablas) en este paquete específico
@EntityScan(basePackages = "com.rokefeli.colmenares.api.entity")
public class ColmenaresBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ColmenaresBackendApplication.class, args);
    }

}