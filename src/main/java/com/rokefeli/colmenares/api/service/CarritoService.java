package com.rokefeli.colmenares.api.service;

import java.util.List;
import com.rokefeli.colmenares.api.entity.Carrito;

public interface CarritoService {
    List<Carrito> findAll();
    Carrito findById(Long id);
    Carrito create(Carrito carrito);
    Carrito update(Long id, Carrito carrito);
    void delete(Long id);
}
