package com.rokefeli.colmenares.api.controller;

import com.rokefeli.colmenares.api.dto.create.CategoriaCreateDTO;
import com.rokefeli.colmenares.api.dto.create.ProductoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.CategoriaResponseDTO;
import com.rokefeli.colmenares.api.dto.response.ProductoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.ProductoUpdateDTO;
import com.rokefeli.colmenares.api.service.interfaces.CategoriaService;
import com.rokefeli.colmenares.api.service.interfaces.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    // --- PRODUCTOS ---

    // Listar todos (PÚBLICO)
    @GetMapping
    public List<ProductoResponseDTO> listarProductos() {
        return productoService.findAll();
    }

    // Crear producto (ADMIN)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"ROLE_ADMIN", "ROLE_EMPLEADO"})
    public ProductoResponseDTO crearProducto(@RequestBody ProductoCreateDTO dto) {
        return productoService.create(dto);
    }

    // Actualizar producto (ADMIN)
    @PutMapping("/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_EMPLEADO"})
    public ProductoResponseDTO actualizarProducto(@PathVariable Long id, @RequestBody ProductoUpdateDTO dto) {
        return productoService.update(id, dto);
    }

    // --- CATEGORÍAS ---

    // Listar categorías (PÚBLICO)
    @GetMapping("/categorias")
    public List<CategoriaResponseDTO> listarCategorias() {
        return categoriaService.findAll();
    }

    // Crear categoría (ADMIN)
    @PostMapping("/categorias")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"ROLE_ADMIN", "ROLE_EMPLEADO"})
    public CategoriaResponseDTO crearCategoria(@RequestBody CategoriaCreateDTO dto) {
        return categoriaService.create(dto);
    }
}