package com.rokefeli.colmenares.api.service;

import java.util.List;
import com.rokefeli.colmenares.api.entity.Pedido;

public interface PedidoService {
    List<Pedido> findAll();
    Pedido findById(Long id);
    Pedido create(Pedido pedido);
    Pedido update(Long id, Pedido pedido);
    void delete(Long id);
}
