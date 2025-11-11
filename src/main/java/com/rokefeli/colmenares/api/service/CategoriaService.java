package com.rokefeli.colmenares.api.service;

import java.util.List;
import com.rokefeli.colmenares.api.entity.Categoria;

public interface CategoriaService {
    List<Categoria> findAll();
    Categoria findById(Long id);
    Categoria create(Categoria categoria);
    Categoria update(Long id, Categoria categoria);
    void delete(Long id);
}
