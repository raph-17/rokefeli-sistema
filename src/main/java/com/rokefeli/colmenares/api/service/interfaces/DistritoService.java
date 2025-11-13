package com.rokefeli.colmenares.api.service.interfaces;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.DistritoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.DistritoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.DistritoUpdateDTO;

public interface DistritoService {
    List<DistritoResponseDTO> findAll();
    DistritoResponseDTO findById(Long id);
    DistritoResponseDTO create(DistritoCreateDTO createDTO);
    DistritoResponseDTO update(Long id, DistritoUpdateDTO updateDTO);
    void delete(Long id);
}
