package com.rokefeli.colmenares.api.repository;

import com.rokefeli.colmenares.api.entity.Categoria;
import com.rokefeli.colmenares.api.entity.enums.EstadoCategoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByEstado(EstadoCategoria estado);
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
}
