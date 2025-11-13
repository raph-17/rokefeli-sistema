package com.rokefeli.colmenares.api.repository;

import com.rokefeli.colmenares.api.entity.Producto;
import com.rokefeli.colmenares.api.entity.enums.EstadoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoria_Id(Long idCategoria);
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    List<Producto> findByEstado(EstadoProducto estado);
}
