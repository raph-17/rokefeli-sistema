package com.rokefeli.colmenares.api.controller;

import com.rokefeli.colmenares.api.dto.create.ProductoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.ProductoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.ProductoUpdateDTO;
import com.rokefeli.colmenares.api.dto.update.StockAdjustmentDTO;
import com.rokefeli.colmenares.api.entity.enums.EstadoProducto;
import com.rokefeli.colmenares.api.service.interfaces.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // Listar todos (ACTIVOS)
    @GetMapping
    public ResponseEntity<?> listarProductosActivos() {
        return ResponseEntity.ok(productoService.findAllActivos());
    }

    // Buscar
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarCliente(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Long idCategoria
    ) {
        return ResponseEntity.ok(productoService.buscarCliente(nombre, idCategoria));
    }

    // Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorIdCliente(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.findByIdCliente(id));
    }

    // ADMIN: Listar todos los productos
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listarProductos() {
        return ResponseEntity.ok(productoService.findAll());
    }

    // ADMIN: Buscar
    @GetMapping("/admin/buscar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> buscarAdmin(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Long idCategoria,
            @RequestParam(required = false) EstadoProducto estado // DESCONTINUADO / ACTIVO
    ) {
        return ResponseEntity.ok(
                productoService.buscarAdmin(nombre, idCategoria, estado)
        );
    }

    // ADMIN: Obtener por ID
    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> obtenerPorIdAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.findById(id));
    }

    // ADMIN: Registrar
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registrarProducto(
            @Valid @RequestBody ProductoCreateDTO dto
    ) {
        ProductoResponseDTO saved = productoService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ADMIN: Actualizar
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoUpdateDTO dto
    ) {
        return ResponseEntity.ok(productoService.update(id, dto));
    }

    // ADMIN: Descontinuar producto
    @PutMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> descontinuarProducto(@PathVariable Long id) {
        productoService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    //ADMIN: Reintegrar producto
    @PutMapping("/{id}/activar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> reintegrarProducto(@PathVariable Long id) {
        productoService.activar(id);
        return ResponseEntity.noContent().build();
    }

    // ADMIN: Ajustar stock
    @PutMapping("/stock/ajustar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> ajustarStock(@Valid @RequestBody StockAdjustmentDTO dto) {
        productoService.ajustarStock(dto);
        return ResponseEntity.noContent().build();
    }

    // ADMIN: Eliminar
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
