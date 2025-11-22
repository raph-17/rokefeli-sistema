package com.rokefeli.colmenares.api.controller;

import com.rokefeli.colmenares.api.dto.create.ProductoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.ProductoResponseDTO;
import com.rokefeli.colmenares.api.service.interfaces.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    // Listar todos los productos
    @GetMapping
    public List<ProductoResponseDTO> listarProductos() {
        return productoService.findAll();
    }

    // Registrar un nuevo producto
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoResponseDTO registrarProducto(@RequestBody ProductoCreateDTO dto) {
        return productoService.create(dto);
    }
}