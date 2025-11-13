package com.rokefeli.colmenares.api.service;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.DepartamentoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.DepartamentoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.DepartamentoUpdateDTO;
import com.rokefeli.colmenares.api.entity.Departamento;

public interface DepartamentoService {
    List<DepartamentoResponseDTO> findAll();
    DepartamentoResponseDTO findById(Long id);
    DepartamentoResponseDTO create(DepartamentoCreateDTO createDTO);
    DepartamentoResponseDTO update(Long id, DepartamentoUpdateDTO updateDTO);
    void delete(Long id);
}
