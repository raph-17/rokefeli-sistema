package com.rokefeli.colmenares.api.service.impl;

import com.rokefeli.colmenares.api.dto.create.PedidoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.PedidoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.PedidoUpdateDTO;
import com.rokefeli.colmenares.api.entity.AgenciaEnvio;
import com.rokefeli.colmenares.api.entity.Distrito;
import com.rokefeli.colmenares.api.entity.Pedido;
import com.rokefeli.colmenares.api.entity.Venta;
import com.rokefeli.colmenares.api.entity.enums.EstadoPedido;
import com.rokefeli.colmenares.api.entity.enums.EstadoVenta;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.PedidoMapper;
import com.rokefeli.colmenares.api.repository.AgenciaEnvioRepository;
import com.rokefeli.colmenares.api.repository.DistritoRepository;
import com.rokefeli.colmenares.api.repository.PedidoRepository;
import com.rokefeli.colmenares.api.repository.VentaRepository;
import com.rokefeli.colmenares.api.service.interfaces.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private DistritoRepository distritoRepository;

    @Autowired
    private AgenciaEnvioRepository agenciaRepository;

    @Autowired
    private PedidoMapper pedidoMapper;

    @Override
    public PedidoResponseDTO findById(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));

        return pedidoMapper.toResponseDTO(pedido);
    }

    @Override
    public List<PedidoResponseDTO> findAll() {
        return pedidoRepository.findAll()
                .stream()
                .map(pedidoMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<PedidoResponseDTO> findByEstado(EstadoPedido estado) {
        return pedidoRepository.findByEstado(estado)
                .stream()
                .map(pedidoMapper::toResponseDTO)
                .toList();
    }

    @Override
    public PedidoResponseDTO create(PedidoCreateDTO dto) {

        Venta venta = ventaRepository.findById(dto.getIdVenta())
                .orElseThrow(() -> new ResourceNotFoundException("Venta", dto.getIdVenta()));

        if (venta.getEstado() != EstadoVenta.PAGADA) {
            throw new IllegalStateException("Solo se puede generar un pedido para una venta pagada.");
        }

        Distrito distrito = distritoRepository.findById(dto.getIdDistrito())
                .orElseThrow(() -> new ResourceNotFoundException("Distrito", dto.getIdDistrito()));

        AgenciaEnvio agenciaEnvio = agenciaRepository.findById(dto.getIdAgenciaEnvio())
                .orElseThrow(() -> new ResourceNotFoundException("AgenciaEnvio", dto.getIdAgenciaEnvio()));

        if (pedidoRepository.existsByVenta_Id(dto.getIdVenta())) {
            throw new IllegalStateException("Esta venta ya tiene un pedido generado.");
        }

        Pedido pedido = pedidoMapper.toEntity(dto);
        pedido.setVenta(venta);
        pedido.setDistrito(distrito);
        pedido.setAgenciaEnvio(agenciaEnvio);
        pedido.setEstado(EstadoPedido.PENDIENTE);

        Pedido saved = pedidoRepository.save(pedido);

        return pedidoMapper.toResponseDTO(saved);
    }

    @Override
    public PedidoResponseDTO update(Long id, PedidoUpdateDTO updateDTO) {
        Pedido existing = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));

        pedidoMapper.updateEntityFromDTO(updateDTO, existing);
        return pedidoMapper.toResponseDTO(pedidoRepository.save(existing));
    }

    @Override
    public PedidoResponseDTO cambiarEstado(Long id, EstadoPedido nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));

        pedido.setEstado(nuevoEstado);

        return pedidoMapper.toResponseDTO(pedidoRepository.save(pedido));
    }

    @Override
    public void delete(Long id) {
        Pedido save = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));

        pedidoRepository.delete(save);
    }
}

