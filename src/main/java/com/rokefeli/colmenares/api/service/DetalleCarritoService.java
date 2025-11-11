package com.rokefeli.colmenares.api.service;

import java.util.List;
import com.rokefeli.colmenares.api.entity.DetalleCarrito;

public interface DetalleCarritoService {
    List<DetalleCarrito> findAll();
    DetalleCarrito findById(Long id);
    DetalleCarrito create(DetalleCarrito detallecarrito);
    DetalleCarrito update(Long id, DetalleCarrito detallecarrito);
    void delete(Long id);
}
