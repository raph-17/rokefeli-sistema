package com.rokefeli.colmenares.api.controller;

import com.rokefeli.colmenares.api.dto.create.VentaInternoCreateDTO;
import com.rokefeli.colmenares.api.dto.create.VentaOnlineCreateDTO;
import com.rokefeli.colmenares.api.dto.response.VentaResponseDTO;
import com.rokefeli.colmenares.api.entity.enums.CanalVenta;
import com.rokefeli.colmenares.api.entity.enums.EstadoVenta;
import com.rokefeli.colmenares.api.security.JwtUserDetails;
import com.rokefeli.colmenares.api.service.impl.ReporteServiceImpl;
import com.rokefeli.colmenares.api.service.interfaces.VentaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @Autowired
    private ReporteServiceImpl reporteService;

    // ==========================================
    //  CLIENTE (Autogestión segura)
    // ==========================================

    // Registrar compra Online.
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

    // Generar venta desde el carrito de compras
    @PostMapping("/crear")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<VentaResponseDTO> crearDesdeCarrito(@AuthenticationPrincipal UserDetails userDetails) {
        Long idUsuario = ((JwtUserDetails) userDetails).getId();

        // Llamamos al servicio que copia los items del carrito a una nueva venta PENDIENTE
        VentaResponseDTO ventaPendiente = ventaService.crearVentaDesdeCarrito(idUsuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(ventaPendiente);
    }

    // Ver historial de ventas propias.
    @GetMapping("/mis-compras")
    public ResponseEntity<?> verMisVentas(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = ((JwtUserDetails) userDetails).getId();
        return ResponseEntity.ok(ventaService.findByUsuario(userId));
    }

    // Ver detalle de una venta específica.
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated() && @securityService.isVentaOwner(authentication, #id)")
    public ResponseEntity<?> obtenerVenta(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.findById(id));
    }

    // ==========================================
    //  ADMIN / EMPLEADO (Gestión Interna)
    // ==========================================

    // ADMIN: Registrar venta interna
    @PostMapping("/interno")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<VentaResponseDTO> registrarVentaInterno(
            @Valid @RequestBody VentaInternoCreateDTO dto
    ) {
        VentaResponseDTO venta = ventaService.registrarInterno(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(venta);
    }

    // ADMIN: Listar todas las ventas
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listarTodasLasVentas() {
        return ResponseEntity.ok(ventaService.findAll());
    }

    // ADMIN: Filtrar por id
    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> obtenerPorIdAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.findById(id));
    }

    // ADMIN: Filtrar por usuario
    @GetMapping("/admin/usuario/{idUsuario}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> obtenerPorUsuarioAdmin(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(ventaService.findByUsuario(idUsuario));
    }

    // ADMIN: Búsqueda avanzada de ventas
    @GetMapping("/admin/buscar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> buscarVentasAdmin(
            @RequestParam(required = false) EstadoVenta estado,
            @RequestParam(required = false) CanalVenta canal,
            @RequestParam(required = false) String dni
    ) {
        return ResponseEntity.ok(ventaService.buscarAdmin(estado, canal, dni));
    }

    // ADMIN: Descargar PDF de una venta
    @GetMapping("/{id}/reporte")
    @PreAuthorize("hasRole('ADMIN')") // Solo admins pueden sacar reportes
    public ResponseEntity<byte[]> descargarReporte(@PathVariable Long id) {

        byte[] pdfBytes = reporteService.generarReporteVentaPdf(id);

        HttpHeaders headers = new HttpHeaders();
        // Inline para ver en navegador, Attachment para forzar descarga
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=venta_" + id + ".pdf");
        headers.setContentType(MediaType.APPLICATION_PDF);

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}