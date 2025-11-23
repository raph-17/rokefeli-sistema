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

    // LISTAR TODOS ACTIVOS
    @GetMapping
    public ResponseEntity<?> listarProductosActivos() {
        return ResponseEntity.ok(productoService.findAllActivos());
    }

    // LISTAR TODOS LOS PRODUCTOS (ADMIN)
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listarProductos() {
        return ResponseEntity.ok(productoService.findAll());
    }

    // BUSCAR (ADMIN)
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

    // BUSCAR (CLIENTE) – solo productos activos
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarCliente(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Long idCategoria
    ) {
        return ResponseEntity.ok(productoService.buscarCliente(nombre, idCategoria));
    }

    // OBTENER POR ID CLIENTE - solo productos activos
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorIdCliente(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.findByIdCliente(id));
    }

    // OBTENER POR ID ADMIN (INCLUYE INACTIVOS)
    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> obtenerPorIdAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.findById(id));
    }

    // REGISTRAR
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registrarProducto(
            @Valid @RequestBody ProductoCreateDTO dto
    ) {
        ProductoResponseDTO saved = productoService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoUpdateDTO dto
    ) {
        return ResponseEntity.ok(productoService.update(id, dto));
    }

    // DESCONTINUAR PRODUCTO
    @PutMapping("/{id}/descontinuar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> descontinuarProducto(@PathVariable Long id) {
        productoService.descontinuar(id);
        return ResponseEntity.noContent().build();
    }

    //REINTEGRAR PRODUCTO
    @PutMapping("/{id}/reintegrar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> reintegrarProducto(@PathVariable Long id) {
        productoService.reintegrar(id);
        return ResponseEntity.noContent().build();
    }

    // AJUSTAR STOCK (+/-)
    @PutMapping("/stock/ajustar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> ajustarStock(@Valid @RequestBody StockAdjustmentDTO dto) {
        productoService.ajustarStock(dto);
        return ResponseEntity.noContent().build();
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
