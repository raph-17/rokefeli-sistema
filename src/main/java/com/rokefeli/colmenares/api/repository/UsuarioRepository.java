package com.rokefeli.colmenares.api.repository;

import com.rokefeli.colmenares.api.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
