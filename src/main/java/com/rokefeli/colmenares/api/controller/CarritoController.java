package com.rokefeli.colmenares.api.controller;

import com.rokefeli.colmenares.api.dto.create.DetalleCarritoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.CarritoResponseDTO;
import com.rokefeli.colmenares.api.service.interfaces.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carrito")
@RequiredArgsConstructor
@Secured({"ROLE_ADMIN", "ROLE_CLIENTE"}) // Todos los métodos requieren CLIENTE o ADMIN
public class CarritoController {

    private final CarritoService carritoService;

    // Método auxiliar para obtener el ID de usuario desde el contexto de seguridad
    private Long getCurrentUserId() {
        // En una implementación real, buscarías el ID asociado al email/username
        // Por simplicidad, asumimos que el servicio lo resuelve o lo pasamos como DTO/Header.
        // Aquí asumiremos que el servicio tiene la lógica para obtener el ID desde el contexto.
        return 1L; // Placeholder
    }

    // Obtener el carrito del usuario actual
    @GetMapping
    public CarritoResponseDTO verCarrito() {
        // En una implementación real, se usaría el ID real del usuario logueado
        return carritoService.verCarrito(getCurrentUserId());
    }

    // Agregar o incrementar un producto en el carrito
    @PostMapping("/agregar")
    @ResponseStatus(HttpStatus.CREATED)
    public CarritoResponseDTO agregarProducto(@RequestBody DetalleCarritoCreateDTO detalleDto) {
        return carritoService.agregarProducto(getCurrentUserId(), detalleDto);
    }

    // Actualizar cantidad de un producto existente
    @PutMapping("/actualizar")
    public CarritoResponseDTO actualizarCantidad(@RequestBody DetalleCarritoCreateDTO detalleDto) {
        // Asumo un método que maneja el DTO de crear para actualizar
        return carritoService.actualizarCantidad(getCurrentUserId(), detalleDto.getIdProducto(), detalleDto.getCantidad());
    }

    // Eliminar un producto del carrito
    @DeleteMapping("/{idProducto}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CarritoResponseDTO eliminarProducto(@PathVariable Long idProducto) {
        return carritoService.eliminarProducto(getCurrentUserId(), idProducto);
    }
}