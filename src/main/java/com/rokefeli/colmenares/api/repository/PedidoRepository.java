package com.rokefeli.colmenares.api.repository;

import com.rokefeli.colmenares.api.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    boolean existsByVenta_Id(Long idVenta);
}
