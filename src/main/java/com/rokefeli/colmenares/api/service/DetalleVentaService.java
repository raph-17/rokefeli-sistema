package com.rokefeli.colmenares.api.service;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.DetalleVentaCreateDTO;
import com.rokefeli.colmenares.api.dto.response.DetalleVentaResponseDTO;
import com.rokefeli.colmenares.api.entity.DetalleVenta;

public interface DetalleVentaService {
    List<DetalleVentaResponseDTO> findAll();
    DetalleVentaResponseDTO findById(Long id);
    DetalleVentaResponseDTO create(DetalleVentaCreateDTO createDTO);
    void delete(Long id);
}
