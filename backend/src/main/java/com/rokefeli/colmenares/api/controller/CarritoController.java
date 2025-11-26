package com.rokefeli.colmenares.api.controller;

import com.rokefeli.colmenares.api.dto.create.DetalleCarritoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.CarritoResponseDTO;
import com.rokefeli.colmenares.api.security.JwtUserDetails;
import com.rokefeli.colmenares.api.service.interfaces.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    // Helper para obtener ID del usuario logueado
    private Long getUserId(UserDetails userDetails) {
        return ((JwtUserDetails) userDetails).getId();
    }

    // Ver MI carrito
    @GetMapping
    public CarritoResponseDTO verMiCarrito(@AuthenticationPrincipal UserDetails userDetails) {
        return carritoService.verCarrito(getUserId(userDetails));
    }

    // Agregar a MI carrito
    @PostMapping("/agregar")
    public CarritoResponseDTO agregarProducto(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody DetalleCarritoCreateDTO dto
    ) {
        return carritoService.agregarProducto(getUserId(userDetails), dto);
    }

    // Actualizar cantidad
    @PutMapping("/producto/{idProducto}")
    public CarritoResponseDTO actualizarCantidad(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long idProducto,
            @RequestParam Integer cantidad
    ) {
        return carritoService.actualizarCantidad(getUserId(userDetails), idProducto, cantidad);
    }

    // Eliminar producto
    @DeleteMapping("/producto/{idProducto}")
    public CarritoResponseDTO eliminarProducto(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long idProducto
    ) {
        return carritoService.eliminarProducto(getUserId(userDetails), idProducto);
    }

    // Vaciar carrito
    @DeleteMapping("/vaciar")
    public void vaciarCarrito(@AuthenticationPrincipal UserDetails userDetails) {
        carritoService.vaciarCarrito(getUserId(userDetails));
    }
}