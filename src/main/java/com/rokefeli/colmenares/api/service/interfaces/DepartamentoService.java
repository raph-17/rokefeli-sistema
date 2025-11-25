package com.rokefeli.colmenares.api.service.interfaces;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.DepartamentoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.DepartamentoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.DepartamentoUpdateDTO;
import com.rokefeli.colmenares.api.entity.enums.EstadoDepartamento;

public interface DepartamentoService {
    List<DepartamentoResponseDTO> findAll();
    List<DepartamentoResponseDTO> findAllActivos();
    DepartamentoResponseDTO findById(Long id);
    List<DepartamentoResponseDTO> findByEstado(EstadoDepartamento estado);
    DepartamentoResponseDTO create(DepartamentoCreateDTO createDTO);
    DepartamentoResponseDTO update(Long id, DepartamentoUpdateDTO updateDTO);
    void desactivar(Long id);
    void activar(Long id);
    void delete(Long id);
}
