package com.rokefeli.colmenares.api.controller;

import com.rokefeli.colmenares.api.dto.create.VentaInternoCreateDTO;
import com.rokefeli.colmenares.api.dto.create.VentaOnlineCreateDTO;
import com.rokefeli.colmenares.api.dto.response.VentaResponseDTO;
import com.rokefeli.colmenares.api.entity.enums.EstadoVenta;
import com.rokefeli.colmenares.api.security.JwtUserDetails; // Asegúrate de importar tu clase
import com.rokefeli.colmenares.api.service.interfaces.VentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    // ==========================================
    //  CLIENTE (Autogestión segura)
    // ==========================================

    /**
     * Registrar compra Online.
     * El ID del usuario se toma del Token para evitar suplantación.
     */
    @PostMapping("/online")
    public ResponseEntity<VentaResponseDTO> registrarVentaOnline(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody VentaOnlineCreateDTO dto
    ) {
        // Sobrescribimos el ID del DTO con el ID real del usuario logueado
        Long userId = ((JwtUserDetails) userDetails).getId();
        dto.setIdUsuario(userId);

        VentaResponseDTO venta = ventaService.registrarOnline(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(venta);
    }

    /**
     * Ver historial de ventas propias.
     * Reemplaza a: /usuario/{idUsuario}
     */
    @GetMapping("/mis-compras")
    public ResponseEntity<?> verMisVentas(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = ((JwtUserDetails) userDetails).getId();
        return ResponseEntity.ok(ventaService.findByUsuario(userId));
    }

    /**
     * Ver detalle de una venta específica.
     * NOTA: Idealmente el servicio debería validar que la venta pertenezca al usuario
     * si el rol es CLIENTE. Por ahora permitimos acceso autenticado.
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated() && @securityService.isVentaOwner(authentication, #id)")
    public ResponseEntity<?> obtenerVenta(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.findById(id));
    }

    // ==========================================
    //  ADMIN / EMPLEADO (Gestión Interna)
    // ==========================================

    @PostMapping("/interno")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<VentaResponseDTO> registrarVentaInterno(
            @Valid @RequestBody VentaInternoCreateDTO dto
    ) {
        VentaResponseDTO venta = ventaService.registrarInterno(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(venta);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listarTodasLasVentas() {
        return ResponseEntity.ok(ventaService.findAll());
    }

    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> obtenerPorIdAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.findById(id));
    }

    @GetMapping("/admin/usuario/{idUsuario}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> obtenerPorUsuarioAdmin(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(ventaService.findByUsuario(idUsuario));
    }

    // Filtro combinado para el panel admin
    @GetMapping("/admin/buscar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> buscarVentas(
            @RequestParam(required = false) Long idUsuario,
            @RequestParam(required = false) EstadoVenta estado
    ) {
        if (idUsuario != null && estado != null) {
            return ResponseEntity.ok(ventaService.findByEstadoCliente(idUsuario, estado));
        } else if (idUsuario != null) {
            return ResponseEntity.ok(ventaService.findByUsuario(idUsuario));
        } else {
            return ResponseEntity.ok(ventaService.findAll());
        }
    }
}