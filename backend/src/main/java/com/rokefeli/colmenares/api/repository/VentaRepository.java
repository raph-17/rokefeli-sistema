package com.rokefeli.colmenares.api.repository;

import com.rokefeli.colmenares.api.entity.Venta;
import com.rokefeli.colmenares.api.entity.enums.CanalVenta;
import com.rokefeli.colmenares.api.entity.enums.EstadoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByUsuario_Id(Long idUsuario);
    List<Venta> findByUsuario_IdAndEstado(Long idUsuario, EstadoVenta estado);
    boolean existsByUsuario_Id(Long idUsuario);

    @Query("""
        SELECT v FROM Venta v
        WHERE (:estado IS NULL OR v.estado = :estado)
        AND (:canal IS NULL OR v.canal = :canal)
        AND (:dni IS NULL OR :dni = '' OR v.usuario.dni LIKE CONCAT('%', :dni, '%'))
    """)
    List<Venta> buscarVentasAdmin(
            @Param("estado") EstadoVenta estado,
            @Param("canal") CanalVenta canal,
            @Param("dni") String dni
    );
}
