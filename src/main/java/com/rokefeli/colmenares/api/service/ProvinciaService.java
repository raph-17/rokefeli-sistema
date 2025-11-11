package com.rokefeli.colmenares.api.service;

import java.util.List;
import com.rokefeli.colmenares.api.entity.Provincia;

public interface ProvinciaService {
    List<Provincia> findAll();
    Provincia findById(Long id);
    Provincia create(Provincia provincia);
    Provincia update(Long id, Provincia provincia);
    void delete(Long id);
}
