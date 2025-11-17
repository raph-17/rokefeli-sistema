package com.rokefeli.colmenares.api.service.interfaces;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.PedidoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.PedidoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.PedidoUpdateDTO;
import com.rokefeli.colmenares.api.entity.enums.EstadoPedido;

public interface PedidoService {
    List<PedidoResponseDTO> findAll();
    PedidoResponseDTO findById(Long id);
    List<PedidoResponseDTO> findByEstado(EstadoPedido estado);
    PedidoResponseDTO create(PedidoCreateDTO createDTO);
    PedidoResponseDTO update(Long id, PedidoUpdateDTO updateDTO);
    PedidoResponseDTO cambiarEstado(Long id, EstadoPedido estado);
    void delete(Long id);

    List<PedidoResponseDTO> findByUsuarioId(long l);
}
