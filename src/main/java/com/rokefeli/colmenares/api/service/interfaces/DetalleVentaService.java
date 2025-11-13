package com.rokefeli.colmenares.api.service.interfaces;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.DetalleVentaCreateDTO;
import com.rokefeli.colmenares.api.dto.response.DetalleVentaResponseDTO;

public interface DetalleVentaService {
    List<DetalleVentaResponseDTO> findAll();
    DetalleVentaResponseDTO findById(Long id);
    DetalleVentaResponseDTO create(DetalleVentaCreateDTO createDTO);
    void delete(Long id);
}
