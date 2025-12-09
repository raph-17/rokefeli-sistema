package com.rokefeli.colmenares.api.controller;

import com.rokefeli.colmenares.api.dto.create.PedidoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.PedidoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.PedidoUpdateDTO;
import com.rokefeli.colmenares.api.entity.enums.EstadoPedido;
import com.rokefeli.colmenares.api.security.JwtUserDetails;
import com.rokefeli.colmenares.api.service.interfaces.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // ==========================================
    //  CLIENTE (Gestión Propia)
    // ==========================================

    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public ResponseEntity<?> crear(@Valid @RequestBody PedidoCreateDTO createDTO) {
        PedidoResponseDTO nuevoPedido = pedidoService.create(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPedido);
    }

    @GetMapping("/mis-pedidos")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> verMisPedidos(@AuthenticationPrincipal UserDetails userDetails) {
        // 2. Uso seguro del ID del token
        Long idUsuario = ((JwtUserDetails) userDetails).getId();
        return ResponseEntity.ok(pedidoService.findByUsuarioId(idUsuario));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.findById(id));
    }

    // ==========================================
    //  ADMIN / EMPLEADO (Logística)
    // ==========================================

    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<?> listarTodos() {
        return ResponseEntity.ok(pedidoService.findAll());
    }

    @GetMapping("/admin/estado")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<?> listarPorEstado(@RequestParam EstadoPedido estado) {
        return ResponseEntity.ok(pedidoService.findByEstado(estado));
    }

    // Cambiar de estado un pedido
    @PatchMapping("/admin/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<?> cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoPedido nuevoEstado
    ) {
        return ResponseEntity.ok(pedidoService.cambiarEstado(id, nuevoEstado));
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PedidoUpdateDTO updateDTO
    ) {
        return ResponseEntity.ok(pedidoService.update(id, updateDTO));
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        pedidoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}