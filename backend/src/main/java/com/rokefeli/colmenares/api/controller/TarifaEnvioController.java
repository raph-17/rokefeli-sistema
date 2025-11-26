package com.rokefeli.colmenares.api.controller;

import com.rokefeli.colmenares.api.dto.create.TarifaEnvioCreateDTO;
import com.rokefeli.colmenares.api.dto.response.TarifaEnvioResponseDTO;
import com.rokefeli.colmenares.api.dto.update.TarifaEnvioUpdateDTO;
import com.rokefeli.colmenares.api.service.interfaces.TarifaEnvioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tarifas-envio")
public class TarifaEnvioController {

    @Autowired
    private TarifaEnvioService tarifaEnvioService;

    // Obtener opciones activas por distrito
    @GetMapping("/distrito/{idDistrito}/disponibles")
    public ResponseEntity<?> listarOpcionesParaCheckout(@PathVariable Long idDistrito) {
        return ResponseEntity.ok(tarifaEnvioService.findByDistritoIdActivo(idDistrito));
    }

    // ADMIN: Obtiene TODAS las tarifas
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listarTodas() {
        return ResponseEntity.ok(tarifaEnvioService.findAll());
    }

    // ADMIN: Obtiene tarifa por id
    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tarifaEnvioService.findById(id));
    }

    // ADMIN: Obtiene tarifa por distrito
    @GetMapping("/admin/distrito/{idDistrito}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listarPorDistrito(@PathVariable Long idDistrito) {
        return ResponseEntity.ok(tarifaEnvioService.findByDistritoId(idDistrito));
    }

    // ADMIN: Obtiene tarifa por agencia
    @GetMapping("/admin/agencia/{idAgencia}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listarPorAgencia(@PathVariable Long idAgencia) {
        return ResponseEntity.ok(tarifaEnvioService.findByAgenciaId(idAgencia));
    }

    // ADMIN: Obtiene tarifa por distrito y agencia (incluye INACTIVAS)
    @GetMapping("/admin/distrito/{idDistrito}/agencia/{idAgencia}")
    public ResponseEntity<?> listarPorDistritoAndAgencia(@PathVariable Long idDistrito,
                                                         @PathVariable Long idAgencia) {
        return ResponseEntity.ok(tarifaEnvioService.findByAgenciaIdAndDistritoId(idDistrito, idAgencia));
    }

    // ADMIN: Registra nueva tarifa
    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> crear(@Valid @RequestBody TarifaEnvioCreateDTO createDTO) {
        TarifaEnvioResponseDTO nuevaTarifa = tarifaEnvioService.create(createDTO);
        return ResponseEntity.ok(nuevaTarifa);
    }

    // ADMIN: Actualiza una tarifa
    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody TarifaEnvioUpdateDTO updateDTO
    ) {
        return ResponseEntity.ok(tarifaEnvioService.update(id, updateDTO));
    }

    // ADMIN: Desactiva una tarifa
    @PutMapping("/admin/{id}/desactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> desactivar(@PathVariable Long id) {
        tarifaEnvioService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    // ADMIN: Activa una tarifa
    @PutMapping("/admin/{id}/activar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activar(@PathVariable Long id) {
        tarifaEnvioService.activar(id);
        return ResponseEntity.noContent().build();
    }

    // ADMIN: Elimina una tarifa
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        tarifaEnvioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}