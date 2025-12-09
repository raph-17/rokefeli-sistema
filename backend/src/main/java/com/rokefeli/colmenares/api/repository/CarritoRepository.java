package com.rokefeli.colmenares.api.repository;

import com.rokefeli.colmenares.api.entity.Carrito;
import com.rokefeli.colmenares.api.entity.enums.EstadoCarrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Optional<Carrito> findByUsuario_IdAndEstado(Long idUsuario, EstadoCarrito estado);

    List<Carrito> findByEstadoAndFechaActualizacionBefore(EstadoCarrito estado, LocalDateTime fechaLimite);
}
