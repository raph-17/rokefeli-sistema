package com.rokefeli.colmenares.api.service.impl;

import com.rokefeli.colmenares.api.dto.create.PedidoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.PedidoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.PedidoUpdateDTO;
import com.rokefeli.colmenares.api.entity.*;
import com.rokefeli.colmenares.api.entity.enums.EstadoAgencia;
import com.rokefeli.colmenares.api.entity.enums.EstadoDistrito;
import com.rokefeli.colmenares.api.entity.enums.EstadoPedido;
import com.rokefeli.colmenares.api.entity.enums.EstadoVenta;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.PedidoMapper;
import com.rokefeli.colmenares.api.repository.*;
import com.rokefeli.colmenares.api.service.interfaces.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private TarifaEnvioRepository tarifaRepository;

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

        Distrito distrito = distritoRepository.findByIdAndEstado(dto.getIdDistrito(), EstadoDistrito.ACTIVO)
                .orElseThrow(() -> new ResourceNotFoundException("Distrito", dto.getIdDistrito()));

        AgenciaEnvio agenciaEnvio = agenciaRepository.findByIdAndEstado(dto.getIdAgenciaEnvio(), EstadoAgencia.ACTIVO)
                .orElseThrow(() -> new ResourceNotFoundException("AgenciaEnvio", dto.getIdAgenciaEnvio()));

        if (pedidoRepository.existsByVenta_Id(dto.getIdVenta())) {
            throw new IllegalStateException("Esta venta ya tiene un pedido generado.");
        }

        TarifaEnvio tarifa = tarifaRepository.findByAgencia_IdAndDistrito_Id(agenciaEnvio.getId(), distrito.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Tarifa"));

        venta.setEstado(EstadoVenta.PROCESADA);
        ventaRepository.save(venta);

        Pedido pedido = pedidoMapper.toEntity(dto);

        pedido.setVenta(venta);
        pedido.setDistrito(distrito);
        pedido.setAgenciaEnvio(agenciaEnvio);
        pedido.setFechaEstimada(LocalDateTime.now().plusDays(tarifa.getDiasEstimados()));
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

        if (pedido.getEstado() == EstadoPedido.ENTREGADO) {
            throw new IllegalStateException("Un pedido entregado no puede cambiar de estado.");
        }

        pedido.setEstado(nuevoEstado);
        if(nuevoEstado == EstadoPedido.ENTREGADO) {
            pedido.setFechaEntrega(LocalDateTime.now());
        }
        return pedidoMapper.toResponseDTO(pedidoRepository.save(pedido));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));

        if (pedido.getEstado() != EstadoPedido.CANCELADO) {
            throw new IllegalStateException("Solo se pueden eliminar pedidos CANCELADOS.");
        }

        pedidoRepository.delete(pedido);
    }

    @Override
    public List<PedidoResponseDTO> findByUsuarioId(Long idUsuario) {

        List<Venta> ventas = ventaRepository.findByUsuario_Id(idUsuario);

        return pedidoRepository.findByVentaIn(ventas)
                .stream()
                .map(pedidoMapper::toResponseDTO)
                .toList();
    }
}

