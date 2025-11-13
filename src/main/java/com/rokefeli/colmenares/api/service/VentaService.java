package com.rokefeli.colmenares.api.service;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.VentaCreateDTO;
import com.rokefeli.colmenares.api.dto.response.VentaResponseDTO;
import com.rokefeli.colmenares.api.entity.Venta;

public interface VentaService {
    List<VentaResponseDTO> findAll();
    VentaResponseDTO findById(Long id);
    VentaResponseDTO create(VentaCreateDTO createDTO);
    void delete(Long id);
}
