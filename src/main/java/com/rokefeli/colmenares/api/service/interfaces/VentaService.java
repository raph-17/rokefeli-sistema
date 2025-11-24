package com.rokefeli.colmenares.api.service.interfaces;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.VentaInternoCreateDTO;
import com.rokefeli.colmenares.api.dto.create.VentaOnlineCreateDTO;
import com.rokefeli.colmenares.api.dto.response.VentaResponseDTO;

public interface VentaService {
    VentaResponseDTO registrarOnline(VentaOnlineCreateDTO dto);

    VentaResponseDTO registrarInterno(VentaInternoCreateDTO dto);

    VentaResponseDTO findById(Long id);

    List<VentaResponseDTO> findAll();

    List<VentaResponseDTO> findByUsuario(Long idUsuario);
}

