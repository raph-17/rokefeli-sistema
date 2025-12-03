package com.rokefeli.colmenares.api;

import com.rokefeli.colmenares.api.entity.Usuario;
import com.rokefeli.colmenares.api.entity.enums.EstadoUsuario;
import com.rokefeli.colmenares.api.entity.enums.Rol;
import com.rokefeli.colmenares.api.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
// 1. Fuerza la búsqueda de repositorios en este paquete específico
@EnableJpaRepositories(basePackages = "com.rokefeli.colmenares.api.repository")
// 2. Fuerza la búsqueda de entidades (Tablas) en este paquete específico
@EntityScan(basePackages = "com.rokefeli.colmenares.api.entity")
// 3. Activa las tareas automaticas
@EnableScheduling
public class ColmenaresBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ColmenaresBackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner initAdmin(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            // ¿Ya existe un admin en el sistema?
            boolean existeAdmin = usuarioRepository.existsByRol(Rol.ADMIN);

            if (!existeAdmin) {

                Usuario admin = new Usuario();
                admin.setNombres("Super");
                admin.setApellidos("Admin");
                admin.setDni("00000000");
                admin.setTelefono("900000000");
                admin.setEmail("admin@system.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRol(Rol.ADMIN);
                admin.setEstado(EstadoUsuario.ACTIVO);

                usuarioRepository.save(admin);

                System.out.println("ADMIN inicial creado: admin@system.com / admin123");
            }
        };
    }


}