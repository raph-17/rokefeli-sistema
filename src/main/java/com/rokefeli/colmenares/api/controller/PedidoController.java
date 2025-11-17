package com.rokefeli.colmenares.api.controller;

import com.rokefeli.colmenares.api.dto.create.PagoCreateDTO;
import com.rokefeli.colmenares.api.dto.create.PedidoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.PagoResponseDTO;
import com.rokefeli.colmenares.api.dto.response.PedidoResponseDTO;
import com.rokefeli.colmenares.api.entity.enums.EstadoPedido;
import com.rokefeli.colmenares.api.service.interfaces.PagoService;
import com.rokefeli.colmenares.api.service.interfaces.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
@Secured({"ROLE_ADMIN", "ROLE_CLIENTE"})
public class PedidoController {

    private final PedidoService pedidoService;
    private final PagoService pagoService;

    // --- PEDIDOS ---

    // Crear un pedido (usualmente después de la compra desde el carrito)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponseDTO crearPedido(@RequestBody PedidoCreateDTO dto) {
        return pedidoService.create(dto);
    }

    // Listar historial de pedidos del usuario (CLIENTE)
    @GetMapping("/historial")
    public List<PedidoResponseDTO> listarHistorialPedidos() {
        // Asumo un método que usa el contexto de seguridad para filtrar por usuario
        return pedidoService.findByUsuarioId(1L); // Placeholder
    }

    // Cambiar estado del pedido (ADMIN/EMPLEADO)
    @PutMapping("/{id}/estado/{nuevoEstado}")
    @Secured({"ROLE_ADMIN", "ROLE_EMPLEADO"})
    public PedidoResponseDTO cambiarEstadoPedido(@PathVariable Long id, @PathVariable String nuevoEstado) {
        // Aquí nuevoEstado debe mapearse al Enum EstadoPedido
        return pedidoService.cambiarEstado(id, EstadoPedido.valueOf(nuevoEstado));
    }

    // --- PAGOS ---

    // Procesar el pago de un pedido (ejecución de la pasarela)
    @PostMapping("/pago")
    public PagoResponseDTO procesarPago(@RequestBody PagoCreateDTO pagoDto) {
        return pagoService.procesarPago(pagoDto);
    }
}