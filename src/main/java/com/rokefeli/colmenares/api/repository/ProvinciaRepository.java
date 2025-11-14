package com.rokefeli.colmenares.api.repository;

import com.rokefeli.colmenares.api.entity.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProvinciaRepository extends JpaRepository<Provincia, Long> {
    List<Provincia> findByDepartamento_Id(Long departamentoId);
    boolean existsByDepartamento_Id(Long departamentoId);
}
