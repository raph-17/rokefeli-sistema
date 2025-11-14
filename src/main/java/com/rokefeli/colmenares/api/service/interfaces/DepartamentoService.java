package com.rokefeli.colmenares.api.service.interfaces;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.DepartamentoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.DepartamentoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.DepartamentoUpdateDTO;

public interface DepartamentoService {
    List<DepartamentoResponseDTO> findAll();
    DepartamentoResponseDTO findById(Long id);
    DepartamentoResponseDTO create(DepartamentoCreateDTO createDTO);
    DepartamentoResponseDTO update(Long id, DepartamentoUpdateDTO updateDTO);
    void desactivar(Long id);
    void activar(Long id);
    void delete(Long id);
}
