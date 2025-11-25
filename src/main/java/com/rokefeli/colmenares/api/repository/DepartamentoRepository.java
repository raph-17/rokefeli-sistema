package com.rokefeli.colmenares.api.repository;

import com.rokefeli.colmenares.api.entity.Departamento;
import com.rokefeli.colmenares.api.entity.enums.EstadoDepartamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
    Optional<Departamento> findByIdAndEstado(Long id, EstadoDepartamento estado);
    List<Departamento> findByEstado(EstadoDepartamento estado);
    boolean existsByNombreIgnoreCase(String nombre);
}
