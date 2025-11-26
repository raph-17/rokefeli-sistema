package com.rokefeli.colmenares.api.service.interfaces;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.DistritoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.DistritoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.DistritoUpdateDTO;
import com.rokefeli.colmenares.api.entity.enums.EstadoDistrito;

public interface DistritoService {
    List<DistritoResponseDTO> findAll();
    List<DistritoResponseDTO> findByProvinciaId(Long id);
    List<DistritoResponseDTO> findByProvinciaIdActivos(Long id);
    List<DistritoResponseDTO> findByEstado(EstadoDistrito estado);
    DistritoResponseDTO findById(Long id);
    DistritoResponseDTO create(DistritoCreateDTO createDTO);
    DistritoResponseDTO update(Long id, DistritoUpdateDTO updateDTO);
    void desactivar(Long id);
    void activar(Long id);
    void delete(Long id);
}
