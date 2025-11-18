package com.rokefeli.colmenares.api.repository;

import com.rokefeli.colmenares.api.entity.Usuario;
import com.rokefeli.colmenares.api.entity.enums.EstadoUsuario;
import com.rokefeli.colmenares.api.entity.enums.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByEstado(EstadoUsuario estado);
    List<Usuario> findByRol(Rol rol);
    Optional<Usuario> findByDni(String dni);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByDni(String dni);
}
