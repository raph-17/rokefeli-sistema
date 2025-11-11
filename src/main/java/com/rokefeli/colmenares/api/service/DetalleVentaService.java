package com.rokefeli.colmenares.api.service;

import java.util.List;
import com.rokefeli.colmenares.api.entity.DetalleVenta;

public interface DetalleVentaService {
    List<DetalleVenta> findAll();
    DetalleVenta findById(Long id);
    DetalleVenta create(DetalleVenta detalleventa);
    DetalleVenta update(Long id, DetalleVenta detalleventa);
    void delete(Long id);
}
