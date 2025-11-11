package com.rokefeli.colmenares.api.service;

import java.util.List;
import com.rokefeli.colmenares.api.entity.Venta;

public interface VentaService {
    List<Venta> findAll();
    Venta findById(Long id);
    Venta create(Venta venta);
    Venta update(Long id, Venta venta);
    void delete(Long id);
}
