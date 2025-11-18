package com.rokefeli.colmenares.api.repository;

import com.rokefeli.colmenares.api.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByUsuario_Id(Long idUsuario);
    boolean existsByUsuario_Id(Long idUsuario);
}
