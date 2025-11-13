package com.rokefeli.colmenares.api.service.interfaces;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.VentaCreateDTO;
import com.rokefeli.colmenares.api.dto.response.VentaResponseDTO;

public interface VentaService {
    List<VentaResponseDTO> findAll();
    VentaResponseDTO findById(Long id);
    VentaResponseDTO create(VentaCreateDTO createDTO);
    void delete(Long id);
}
