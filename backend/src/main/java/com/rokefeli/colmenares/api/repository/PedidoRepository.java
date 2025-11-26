package com.rokefeli.colmenares.api.repository;

import com.rokefeli.colmenares.api.entity.Pedido;
import com.rokefeli.colmenares.api.entity.Venta;
import com.rokefeli.colmenares.api.entity.enums.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByEstado(EstadoPedido estado);
    List<Pedido> findByVentaIn(List<Venta> ventas);
    boolean existsByVenta_Id(Long idVenta);
}
