package com.rokefeli.colmenares.api.repository;

import com.rokefeli.colmenares.api.entity.Producto;
import com.rokefeli.colmenares.api.entity.enums.EstadoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    @Query("""
    SELECT p FROM Producto p
    WHERE (:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
    AND (:idCategoria IS NULL OR p.categoria.id = :idCategoria)
    AND (:estado IS NULL OR p.estado = :estado)
    """)
    List<Producto> buscarProductos(
            @Param("nombre") String nombre,
            @Param("idCategoria") Long idCategoria,
            @Param("estado") EstadoProducto estado
    );

    boolean existsByCategoria_Id(Long idCategoria);
}
