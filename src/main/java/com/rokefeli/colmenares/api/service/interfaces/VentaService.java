package com.rokefeli.colmenares.api.service.interfaces;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.VentaCreateDTO;
import com.rokefeli.colmenares.api.dto.response.VentaResponseDTO;

public interface VentaService {
    VentaResponseDTO registrar(VentaCreateDTO dto);

    VentaResponseDTO findById(Long id);

    List<VentaResponseDTO> findAll();

    List<VentaResponseDTO> findByUsuario(Long idUsuario);
}

