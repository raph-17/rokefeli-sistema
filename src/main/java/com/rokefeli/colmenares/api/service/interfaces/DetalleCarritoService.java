package com.rokefeli.colmenares.api.service.interfaces;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.DetalleCarritoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.DetalleCarritoResponseDTO;

public interface DetalleCarritoService {
    List<DetalleCarritoResponseDTO> findAll();
    DetalleCarritoResponseDTO findById(Long id);
    DetalleCarritoResponseDTO create(DetalleCarritoCreateDTO createDTO);
    void delete(Long id);
}
