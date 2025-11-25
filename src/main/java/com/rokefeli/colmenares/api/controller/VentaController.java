package com.rokefeli.colmenares.api.controller;

import com.rokefeli.colmenares.api.dto.create.VentaInternoCreateDTO;
import com.rokefeli.colmenares.api.dto.create.VentaOnlineCreateDTO;
import com.rokefeli.colmenares.api.dto.response.VentaResponseDTO;
import com.rokefeli.colmenares.api.entity.enums.EstadoVenta;
import com.rokefeli.colmenares.api.service.interfaces.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    // Realizar Venta CLIENTE
    @PostMapping("/online")
    @PreAuthorize("@securityService.isSelf(authentication, #dto.idUsuario)")
    public ResponseEntity<?> registrarVentaOnline(
            @Valid @RequestBody VentaOnlineCreateDTO dto
    ) {
        VentaResponseDTO venta = ventaService.registrarOnline(dto);
        return ResponseEntity.ok(venta);
    }

    // Ver propia venta (o compra)
    @GetMapping("/{id}")
    @PreAuthorize("@securityService.isVentaOwner(authentication, #id)")
    public ResponseEntity<?> obtenerVenta(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.findById(id));
    }

    // Ventas por usuario
    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("@securityService.isSelf(authentication, #idUsuario)")
    public ResponseEntity<?> ventasPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(ventaService.findByUsuario(idUsuario));
    }

    // Ventas de usuario filtradas por estado
    @GetMapping("/usuario/{idUsuario}/estado")
    @PreAuthorize("@securityService.isSelf(authentication, #idUsuario)")
    public ResponseEntity<?> ventasUsuarioPorEstado(@PathVariable Long idUsuario,
                                                    @RequestParam EstadoVenta estado) {
        return ResponseEntity.ok(ventaService.findByEstadoCliente(idUsuario, estado));
    }

    // ADMIN o EMPLEADO: Registrar venta interna
    @PostMapping("/interno")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<?> registrarVentaInterno(
            @Valid @RequestBody VentaInternoCreateDTO dto
    ) {
        VentaResponseDTO venta = ventaService.registrarInterno(dto);
        return ResponseEntity.ok(venta);
    }

    // ADMIN: Lista TODAS las ventas
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listarVentas() {
        return ResponseEntity.ok(ventaService.findAll());
    }

    // ADMIN: Filtra ventas por ID
    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.findById(id));
    }

    // ADMIN: Filtra ventas por usuario
    @GetMapping("/admin/usuario/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> obtenerPorUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.findByUsuario(id));
    }

    // ADMIN: Filtra ventas por usuario y estado
    @GetMapping("/admin/usuario/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> obtenerPorUsuario(@PathVariable Long id,
                                               @RequestParam EstadoVenta estado) {
        return ResponseEntity.ok(ventaService.findByEstadoCliente(id, estado));
    }
}
