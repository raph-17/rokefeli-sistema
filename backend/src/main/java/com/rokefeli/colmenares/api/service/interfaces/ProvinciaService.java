package com.rokefeli.colmenares.api.service.interfaces;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.ProvinciaCreateDTO;
import com.rokefeli.colmenares.api.dto.response.ProvinciaResponseDTO;
import com.rokefeli.colmenares.api.dto.update.ProvinciaUpdateDTO;

public interface ProvinciaService {
    List<ProvinciaResponseDTO> findAll();

    List<ProvinciaResponseDTO> findByDepartamentoId(Long id);

    List<ProvinciaResponseDTO> findByDepartamentoIdActivos(Long id);

    ProvinciaResponseDTO findById(Long id);

    ProvinciaResponseDTO create(ProvinciaCreateDTO createDTO);

    ProvinciaResponseDTO update(Long id, ProvinciaUpdateDTO updateDTO);

    void desactivar(Long id);

    void activar(Long id);

    void delete(Long id);
}
