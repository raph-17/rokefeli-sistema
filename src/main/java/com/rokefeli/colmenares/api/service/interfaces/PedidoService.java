package com.rokefeli.colmenares.api.service.interfaces;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.PedidoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.PedidoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.PedidoUpdateDTO;

public interface PedidoService {
    List<PedidoResponseDTO> findAll();
    PedidoResponseDTO findById(Long id);
    PedidoResponseDTO create(PedidoCreateDTO createDTO);
    PedidoResponseDTO update(Long id, PedidoUpdateDTO updateDTO);
    void delete(Long id);
}
