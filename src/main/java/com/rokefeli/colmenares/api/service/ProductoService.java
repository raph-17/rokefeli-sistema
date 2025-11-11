package com.rokefeli.colmenares.api.service;

import java.util.List;
import com.rokefeli.colmenares.api.entity.Producto;

public interface ProductoService {
    List<Producto> findAll();
    Producto findById(Long id);
    Producto create(Producto producto);
    Producto update(Long id, Producto producto);
    void delete(Long id);
}
