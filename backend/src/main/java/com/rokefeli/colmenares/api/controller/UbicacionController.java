package com.rokefeli.colmenares.api.controller;

import com.rokefeli.colmenares.api.dto.create.DepartamentoCreateDTO;
import com.rokefeli.colmenares.api.dto.create.DistritoCreateDTO;
import com.rokefeli.colmenares.api.dto.create.ProvinciaCreateDTO;
import com.rokefeli.colmenares.api.service.interfaces.DepartamentoService;
import com.rokefeli.colmenares.api.service.interfaces.DistritoService;
import com.rokefeli.colmenares.api.service.interfaces.ProvinciaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ubicaciones")
public class UbicacionController {

    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private ProvinciaService provinciaService;

    @Autowired
    private DistritoService distritoService;

    // ==========================================
    //  PÚBLICO (Para Selects en Cascada)
    // ==========================================

    // Listar departamentos Activos
    @GetMapping("/departamentos")
    public ResponseEntity<?> listarDepartamentosActivos() {
        return ResponseEntity.ok(departamentoService.findAllActivos());
    }

    // Listar provincias Activas por departamento
    @GetMapping("/provincias/departamento/{idDepartamento}")
    public ResponseEntity<?> listarProvinciasPorDepartamentoActivos(@PathVariable Long idDepartamento) {
        return ResponseEntity.ok(provinciaService.findByDepartamentoIdActivos(idDepartamento));
    }

    @GetMapping("/distritos/provincia/{idProvincia}")
    public ResponseEntity<?> listarDistritosPorProvinciaActivos(@PathVariable Long idProvincia) {
        return ResponseEntity.ok(distritoService.findByProvinciaIdActivos(idProvincia));
    }

    // ==========================================
    //  ADMIN (Gestión de Estados)
    // ==========================================

    // --- DEPARTAMENTOS ---
    @PutMapping("/admin/departamentos/{id}/activar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activarDepartamento(@PathVariable Long id) {
        departamentoService.activar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/admin/departamentos/{id}/desactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> desactivarDepartamento(@PathVariable Long id) {
        departamentoService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    // --- PROVINCIAS ---
    @PutMapping("/admin/provincias/{id}/activar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activarProvincia(@PathVariable Long id) {
        provinciaService.activar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/admin/provincias/{id}/desactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> desactivarProvincia(@PathVariable Long id) {
        provinciaService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    // --- DISTRITOS ---
    @PutMapping("/admin/distritos/{id}/activar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activarDistrito(@PathVariable Long id) {
        distritoService.activar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/admin/distritos/{id}/desactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> desactivarDistrito(@PathVariable Long id) {
        distritoService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    // ==========================================
    // ADMIN (Registros)
    // ==========================================

    // Departamento
    @PostMapping("/admin/departamentos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registrarDepartamento(@Valid @RequestBody DepartamentoCreateDTO dto) {
        return ResponseEntity.ok(departamentoService.create(dto));
    }

    // Provincia
    @PostMapping("/admin/provincias")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registrarProvincia(@Valid @RequestBody ProvinciaCreateDTO dto) {
        return ResponseEntity.ok(provinciaService.create(dto));
    }

    // Distrito
    @PostMapping("/admin/distritos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registrarDistritos(@Valid @RequestBody DistritoCreateDTO dto) {
        return ResponseEntity.ok(distritoService.create(dto));
    }

    // ==========================================
    //  ADMIN (Para Selects en Cascada incluyendo INACTIVOS)
    // ==========================================

    @GetMapping("/admin/departamentos")
    public ResponseEntity<?> listarDepartamentos() {
        return ResponseEntity.ok(departamentoService.findAll());
    }

    @GetMapping("/admin/provincias/departamento/{idDepartamento}")
    public ResponseEntity<?> listarProvinciasPorDepartamento(@PathVariable Long idDepartamento) {
        return ResponseEntity.ok(provinciaService.findByDepartamentoId(idDepartamento));
    }

    @GetMapping("/admin/distritos/provincia/{idProvincia}")
    public ResponseEntity<?> listarDistritosPorProvincia(@PathVariable Long idProvincia) {
        return ResponseEntity.ok(distritoService.findByProvinciaId(idProvincia));
    }
}