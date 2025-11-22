package com.rokefeli.colmenares.api.controller;

import com.rokefeli.colmenares.api.dto.create.DetalleCarritoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.CarritoResponseDTO;
import com.rokefeli.colmenares.api.service.interfaces.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    // ✔ Ver carrito del usuario autenticado
    @GetMapping("/{idUsuario}")
    @PreAuthorize("@securityService.isSelf(authentication, #idUsuario)")
    public CarritoResponseDTO verCarrito(@PathVariable Long idUsuario) {
        return carritoService.verCarrito(idUsuario);
    }

    // ✔ Agregar producto al carrito
    @PostMapping("/{idUsuario}/agregar")
    @PreAuthorize("@securityService.isSelf(authentication, #idUsuario)")
    public CarritoResponseDTO agregarProducto(
            @PathVariable Long idUsuario,
            @RequestBody DetalleCarritoCreateDTO dto
    ) {
        return carritoService.agregarProducto(idUsuario, dto);
    }

    // ✔ Actualizar cantidad
    @PutMapping("/{idUsuario}/producto/{idProducto}")
    @PreAuthorize("@securityService.isSelf(authentication, #idUsuario)")
    public CarritoResponseDTO actualizarCantidad(
            @PathVariable Long idUsuario,
            @PathVariable Long idProducto,
            @RequestParam Integer cantidad
    ) {
        return carritoService.actualizarCantidad(idUsuario, idProducto, cantidad);
    }

    // ✔ Eliminar producto del carrito
    @DeleteMapping("/{idUsuario}/producto/{idProducto}")
    @PreAuthorize("@securityService.isSelf(authentication, #idUsuario)")
    public CarritoResponseDTO eliminarProducto(
            @PathVariable Long idUsuario,
            @PathVariable Long idProducto
    ) {
        return carritoService.eliminarProducto(idUsuario, idProducto);
    }

    // ✔ Vaciar carrito
    @DeleteMapping("/{idUsuario}/vaciar")
    @PreAuthorize("@securityService.isSelf(authentication, #idUsuario)")
    public void vaciarCarrito(@PathVariable Long idUsuario) {
        carritoService.vaciarCarrito(idUsuario);
    }

    // ✔ Marcar como comprado
    @PutMapping("/{idUsuario}/comprar")
    @PreAuthorize("@securityService.isSelf(authentication, #idUsuario)")
    public void marcarComoComprado(@PathVariable Long idUsuario) {
        carritoService.marcarComoComprado(idUsuario);
    }
}
