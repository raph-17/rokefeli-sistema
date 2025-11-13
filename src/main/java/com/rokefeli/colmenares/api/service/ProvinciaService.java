package com.rokefeli.colmenares.api.service;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.ProvinciaCreateDTO;
import com.rokefeli.colmenares.api.dto.response.ProvinciaResponseDTO;
import com.rokefeli.colmenares.api.dto.update.ProvinciaUpdateDTO;

public interface ProvinciaService {
    List<ProvinciaResponseDTO> findAll();
    ProvinciaResponseDTO findById(Long id);
    ProvinciaResponseDTO create(ProvinciaCreateDTO createDTO);
    ProvinciaResponseDTO update(Long id, ProvinciaUpdateDTO updateDTO);
    void delete(Long id);
}
