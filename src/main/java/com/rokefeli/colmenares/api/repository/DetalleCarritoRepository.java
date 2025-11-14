package com.rokefeli.colmenares.api.repository;

import com.rokefeli.colmenares.api.entity.DetalleCarrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DetalleCarritoRepository extends JpaRepository<DetalleCarrito, Long> {

    Optional<DetalleCarrito> findByCarrito_IdAndProducto_Id(Long idCarrito, Long idProducto);

}
