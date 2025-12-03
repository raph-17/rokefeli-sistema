package com.rokefeli.colmenares.api.service.impl;

import com.rokefeli.colmenares.api.dto.create.PagoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.PagoResponseDTO;
import com.rokefeli.colmenares.api.entity.*;
import com.rokefeli.colmenares.api.entity.enums.EstadoPago;
import com.rokefeli.colmenares.api.entity.enums.EstadoTarifa;
import com.rokefeli.colmenares.api.entity.enums.EstadoVenta;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.PagoMapper;
import com.rokefeli.colmenares.api.repository.PagoRepository;
import com.rokefeli.colmenares.api.repository.ProductoRepository;
import com.rokefeli.colmenares.api.repository.TarifaEnvioRepository;
import com.rokefeli.colmenares.api.repository.VentaRepository;
import com.rokefeli.colmenares.api.service.interfaces.CarritoService;
import com.rokefeli.colmenares.api.service.interfaces.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
    private CarritoService carritoService;

    @Autowired
    private PagoMapper pagoMapper;

    @Value("${culqi.api.key}")
    private String culqiKey;

    @Value("${culqi.api.url}")
    private String culqiUrl;

    @Override
    public PagoResponseDTO procesarPago(PagoCreateDTO dto) {

        Venta venta = ventaRepository.findById(dto.getIdVenta())
                .orElseThrow(() -> new ResourceNotFoundException("Venta", dto.getIdVenta()));

        if (venta.getEstado() != EstadoVenta.PENDIENTE) {
            throw new IllegalStateException("La venta no está disponible para pago.");
        }

        TarifaEnvio tarifa = tarifaRepository.findByIdAndEstado(dto.getIdTarifaEnvio(), EstadoTarifa.ACTIVO)
                .orElseThrow(() -> new ResourceNotFoundException("Tarifa", dto.getIdTarifaEnvio()));

        if (dto.getMonto().compareTo(venta.getMontoTotal().add(tarifa.getCostoEnvio())) != 0) {
            throw new IllegalArgumentException("El monto enviado no coincide con el monto total de la venta.");
        }

        Pago pago = pagoMapper.toEntity(dto);
        pago.setVenta(venta);
        pago.setTarifaEnvio(tarifa);
        pago.setEstadoPago(EstadoPago.PENDIENTE);

        boolean aprobado = procesarConPasarelaExterna(dto);

        if (aprobado) {
            pago.setFechaPago(LocalDateTime.now());
            pago.setEstadoPago(EstadoPago.APROBADO);
            venta.setEstado(EstadoVenta.PAGADA);
            carritoService.marcarComoComprado(venta.getUsuario().getId());
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
        try {
            RestTemplate restTemplate = new RestTemplate();

            // 1. Preparar Headers (Autorización)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + culqiKey);

            // 2. Preparar el Cuerpo de la Petición (El Cargo)
            // Culqi pide el monto en CENTIMOS (S/ 100.00 -> 10000)
            HttpEntity<Map<String, Object>> request = getMapHttpEntity(dto, headers);

            // 3. Enviar Petición a Culqi
            String urlCrearCargo = culqiUrl + "/charges";
            ResponseEntity<Map> response = restTemplate.postForEntity(urlCrearCargo, request, Map.class);

            // 4. Validar Respuesta
            if (response.getStatusCode() == HttpStatus.CREATED) {
                return true; // ¡Pago Exitoso!
            } else {
                return false;
            }

        } catch (Exception e) {
            // Manejo de errores (tarjeta rechazada, sin fondos, error de red)
            System.err.println("Error al procesar pago con Culqi: " + e.getMessage());
            return false;
        }
    }

    private static HttpEntity<Map<String, Object>> getMapHttpEntity(PagoCreateDTO dto, HttpHeaders headers) {
        long montoEnCentimos = dto.getMonto().multiply(new java.math.BigDecimal(100)).longValue();

        Map<String, Object> body = new HashMap<>();
        body.put("amount", montoEnCentimos);
        body.put("currency_code", "PEN"); // Moneda Soles
        body.put("email", dto.getEmailCliente());
        body.put("source_id", dto.getTokenCulqi()); // El token de la tarjeta
        body.put("description", "Colmenares Rokefeli - Venta #" + dto.getIdVenta()); // Ej: "Venta #123"
        body.put("capture", true); // Cobrar inmediatamente

        return new HttpEntity<>(body, headers);
    }
}
