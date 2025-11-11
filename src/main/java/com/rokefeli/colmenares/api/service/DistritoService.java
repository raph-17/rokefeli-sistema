package com.rokefeli.colmenares.api.service;

import java.util.List;
import com.rokefeli.colmenares.api.entity.Distrito;

public interface DistritoService {
    List<Distrito> findAll();
    Distrito findById(Long id);
    Distrito create(Distrito distrito);
    Distrito update(Long id, Distrito distrito);
    void delete(Long id);
}
