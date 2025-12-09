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
        // 1. Validaciones previas (Venta, Tarifa, Monto)
        Venta venta = ventaRepository.findById(dto.getIdVenta())
                .orElseThrow(() -> new ResourceNotFoundException("Venta", dto.getIdVenta()));

        if (venta.getEstado() != EstadoVenta.PENDIENTE) {
            throw new IllegalStateException("La venta no está disponible para pago (Ya fue pagada o cancelada).");
        }

        TarifaEnvio tarifa = tarifaRepository.findByIdAndEstado(dto.getIdTarifaEnvio(), EstadoTarifa.ACTIVO)
                .orElseThrow(() -> new ResourceNotFoundException("Tarifa", dto.getIdTarifaEnvio()));

        // Validación de monto (Suma de productos + envío)
        if (dto.getMonto().compareTo(venta.getMontoTotal().add(tarifa.getCostoEnvio())) != 0) {
            throw new IllegalArgumentException("Error de seguridad: El monto no coincide con el total.");
        }

        // 2. Preparar entidad Pago
        Pago pago = pagoMapper.toEntity(dto);
        pago.setVenta(venta);
        pago.setTarifaEnvio(tarifa);
        pago.setEstadoPago(EstadoPago.PENDIENTE);
        pago.setFechaPago(LocalDateTime.now()); // Guardamos fecha intento

        try {
            boolean aprobado = procesarConPasarelaExterna(dto);

            if (aprobado) {
                // ✅ PAGO EXITOSO
                pago.setEstadoPago(EstadoPago.APROBADO);
                pago.setFechaPago(LocalDateTime.now());
                venta.setEstado(EstadoVenta.PAGADA);

                // Cerramos el carrito (Aquí muere la reserva y se convierte en venta)
                carritoService.marcarComoComprado(venta.getUsuario().getId());

            } else {
                // ❌ PAGO RECHAZADO (Tarjeta sin fondos, etc.)
                pago.setEstadoPago(EstadoPago.RECHAZADO);

                // ¡OJO AQUÍ! No cancelamos la venta ni devolvemos stock todavía.
                // Dejamos la venta en PENDIENTE o creamos un estado FALLIDO_TEMPORAL,
                // porque el usuario va a reintentar pagar esta misma venta en 5 segundos.

                // Opción A: Dejarla PENDIENTE para reintento inmediato
                venta.setEstado(EstadoVenta.PENDIENTE);

                // Opción B (Más estricta): Cancelar esta venta, pero NO devolver stock al inventario general
                // porque los items siguen en el carrito. (Más complejo de manejar).

                // RECOMENDACIÓN: No hagas nada con el stock aquí.
                // El stock sigue reservado en el carrito del usuario.
            }

            pagoRepository.save(pago);
            ventaRepository.save(venta);

        } catch (Exception e) {
            // 🚨 ERROR CRÍTICO DEL SISTEMA
            // Aquí sí podríamos devolver stock si decidimos que esto es irrecuperable
            System.err.println("Error procesando pago: " + e.getMessage());
            throw e;
        }

        return pagoMapper.toResponseDTO(pago);
    }

    // --- MÉTODOS AUXILIARES ---

    private void cancelarVentaYDevolverStock(Venta venta) {
        venta.setEstado(EstadoVenta.CANCELADA);

        // Devolvemos el stock de cada producto reservado
        for (DetalleVenta det : venta.getDetalles()) {
            Producto p = det.getProducto();
            p.setStockActual(p.getStockActual() + det.getCantidad());
            productoRepository.save(p);
        }
    }

    @SuppressWarnings({"rawtypes", "RedundantIfStatement"})
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
