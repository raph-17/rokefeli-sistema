package com.rokefeli.colmenares.api.service;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.PedidoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.PedidoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.PedidoUpdateDTO;
import com.rokefeli.colmenares.api.entity.Pedido;

public interface PedidoService {
    List<PedidoResponseDTO> findAll();
    PedidoResponseDTO findById(Long id);
    PedidoResponseDTO create(PedidoCreateDTO createDTO);
    PedidoResponseDTO update(Long id, PedidoUpdateDTO updateDTO);
    void delete(Long id);
}
