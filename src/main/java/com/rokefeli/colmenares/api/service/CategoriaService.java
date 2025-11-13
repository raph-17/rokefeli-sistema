package com.rokefeli.colmenares.api.service;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.CategoriaCreateDTO;
import com.rokefeli.colmenares.api.dto.response.CategoriaResponseDTO;
import com.rokefeli.colmenares.api.dto.update.CategoriaUpdateDTO;

public interface CategoriaService {
    List<CategoriaResponseDTO> findAll();
    CategoriaResponseDTO findById(Long id);
    CategoriaResponseDTO create(CategoriaCreateDTO createDTO);
    CategoriaResponseDTO update(Long id, CategoriaUpdateDTO updateDTO);
    void hardDelete(Long id);
}
