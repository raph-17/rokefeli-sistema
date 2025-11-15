package com.rokefeli.colmenares.api.service.impl;

import com.rokefeli.colmenares.api.dto.create.PagoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.PagoResponseDTO;
import com.rokefeli.colmenares.api.entity.*;
import com.rokefeli.colmenares.api.entity.enums.EstadoPago;
import com.rokefeli.colmenares.api.entity.enums.EstadoVenta;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.PagoMapper;
import com.rokefeli.colmenares.api.repository.PagoRepository;
import com.rokefeli.colmenares.api.repository.ProductoRepository;
import com.rokefeli.colmenares.api.repository.TarifaEnvioRepository;
import com.rokefeli.colmenares.api.repository.VentaRepository;
import com.rokefeli.colmenares.api.service.interfaces.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class PagoServiceImpl implements PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private TarifaEnvioRepository tarifaRepository;

    @Autowired
    private PagoMapper pagoMapper;

    @Override
    public PagoResponseDTO procesarPago(PagoCreateDTO dto) {

        Venta venta = ventaRepository.findById(dto.getIdVenta())
                .orElseThrow(() -> new ResourceNotFoundException("Venta", dto.getIdVenta()));

        if (venta.getEstado() != EstadoVenta.PENDIENTE) {
            throw new IllegalStateException("La venta no está disponible para pago.");
        }

        TarifaEnvio tarifa = tarifaRepository.findById(dto.getIdTarifaEnvio())
                .orElseThrow(() -> new ResourceNotFoundException("Tarifa", dto.getIdTarifaEnvio()));

        if (dto.getMonto().compareTo(venta.getMontoTotal().add(tarifa.getCostoEnvio())) != 0) {
            throw new IllegalArgumentException("El monto enviado no coincide con el monto total de la venta.");
        }

        Pago pago = pagoMapper.toEntity(dto);
        pago.setVenta(venta);
        pago.setEstadoPago(EstadoPago.PENDIENTE);

        boolean aprobado = procesarConPasarelaExterna(dto);

        if (aprobado) {
            pago.setFechaPago(LocalDateTime.now());
            pago.setEstadoPago(EstadoPago.APROBADO);
            venta.setEstado(EstadoVenta.PAGADA);
        }
        else {
            pago.setEstadoPago(EstadoPago.RECHAZADO);
            venta.setEstado(EstadoVenta.CANCELADA);

            for (DetalleVenta det : venta.getDetalles()) {
                Producto p = det.getProducto();
                p.setStockActual(p.getStockActual() + det.getCantidad());
                productoRepository.save(p);
            }
        }

        pagoRepository.save(pago);
        ventaRepository.save(venta);

        return pagoMapper.toResponseDTO(pago);
    }

    private boolean procesarConPasarelaExterna(PagoCreateDTO dto) {
        return true; // simular aprobado
    }
}
