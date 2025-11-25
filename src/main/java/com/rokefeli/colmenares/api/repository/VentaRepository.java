package com.rokefeli.colmenares.api.repository;

import com.rokefeli.colmenares.api.entity.Venta;
import com.rokefeli.colmenares.api.entity.enums.EstadoVenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByUsuario_Id(Long idUsuario);
    List<Venta> findByUsuario_IdAndEstado(Long idUsuario, EstadoVenta estado);
    boolean existsByUsuario_Id(Long idUsuario);
}
