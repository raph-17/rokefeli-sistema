package com.rokefeli.colmenares.api.repository;

import com.rokefeli.colmenares.api.entity.Distrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistritoRepository extends JpaRepository<Distrito, Long> {
    List<Distrito> findByProvincia_Id(Long provinciaId);
}
