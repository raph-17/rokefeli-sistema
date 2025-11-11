package com.rokefeli.colmenares.api.service;

import java.util.List;
import com.rokefeli.colmenares.api.entity.Departamento;

public interface DepartamentoService {
    List<Departamento> findAll();
    Departamento findById(Long id);
    Departamento create(Departamento departamento);
    Departamento update(Long id, Departamento departamento);
    void delete(Long id);
}
