package com.rokefeli.colmenares.api.service;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.DetalleCarritoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.DetalleCarritoResponseDTO;
import com.rokefeli.colmenares.api.entity.DetalleCarrito;

public interface DetalleCarritoService {
    List<DetalleCarritoResponseDTO> findAll();
    DetalleCarritoResponseDTO findById(Long id);
    DetalleCarritoResponseDTO create(DetalleCarritoCreateDTO createDTO);
    void delete(Long id);
}
