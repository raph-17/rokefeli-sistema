package com.rokefeli.colmenares.api.controller;

import com.rokefeli.colmenares.api.dto.create.AgenciaEnvioCreateDTO;
import com.rokefeli.colmenares.api.dto.response.AgenciaEnvioResponseDTO;
import com.rokefeli.colmenares.api.dto.update.AgenciaEnvioUpdateDTO;
import com.rokefeli.colmenares.api.entity.enums.EstadoAgencia;
import com.rokefeli.colmenares.api.service.interfaces.AgenciaEnvioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agencias")
@RequiredArgsConstructor
public class AgenciaEnvioController {

    private final AgenciaEnvioService agenciaService;

    // Listar agencias públicas
    @GetMapping
    public ResponseEntity<?> listarAgenciasActivas() {
        List<AgenciaEnvioResponseDTO> agenciasPublicas = agenciaService.findAllActivos();
        return ResponseEntity.ok(agenciasPublicas);
    }

    // ADMIN: Listar agencias (todas)
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listarAgencias() {
        List<AgenciaEnvioResponseDTO> agencias = agenciaService.findAll();
        return ResponseEntity.ok(agencias);
    }

    // ADMIN: Buscar agencia por ID
    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        AgenciaEnvioResponseDTO agencia = agenciaService.findById(id);
        return ResponseEntity.ok(agencia);
    }

    // ADMIN: Filtrar agencias por estado
    @GetMapping("/admin/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> obtenerPorEstado(@RequestParam EstadoAgencia estado) {
        List<AgenciaEnvioResponseDTO> agencias = agenciaService.findByEstado(estado);
        return ResponseEntity.ok(agencias);
    }

    // ADMIN: Registrar agencia
    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registrarAgencia(@Valid @RequestBody AgenciaEnvioCreateDTO dto) {
        AgenciaEnvioResponseDTO nuevaAgencia = agenciaService.create(dto);
        return ResponseEntity.ok(nuevaAgencia);
    }

    // ADMIN: Actualizar agencia
    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarAgencia(
            @RequestBody AgenciaEnvioUpdateDTO dto,
            @PathVariable Long id
    ) {
        AgenciaEnvioResponseDTO agenciaUpdate = agenciaService.update(id, dto);
        return ResponseEntity.ok(agenciaUpdate);
    }

    // ADMIN: Desactivar agencia
    @PutMapping("/admin/{id}/desactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> desactivarAgencia(@PathVariable Long id) {
        agenciaService.desactivar(id);
        return ResponseEntity.ok("Agencia desactivada.");
    }

    // ADMIN: Activar agencia
    @PutMapping("/admin/{id}/activar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activarAgencia(@PathVariable Long id) {
        agenciaService.activar(id);
        return ResponseEntity.ok("Agencia activada.");
    }

    // ADMIN: Eliminar agencia
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminarAgencia(@PathVariable Long id) {
        agenciaService.delete(id);
        return ResponseEntity.ok("Agencia eliminada.");
    }
}