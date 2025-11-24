package com.rokefeli.colmenares.api.controller;

import com.rokefeli.colmenares.api.dto.create.CategoriaCreateDTO;
import com.rokefeli.colmenares.api.dto.response.CategoriaResponseDTO;
import com.rokefeli.colmenares.api.dto.update.CategoriaUpdateDTO;
import com.rokefeli.colmenares.api.entity.enums.EstadoCategoria;
import com.rokefeli.colmenares.api.service.interfaces.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // Listar todas las categorías ACTIVAS (para clientes)
    @GetMapping
    public List<CategoriaResponseDTO> listarCategoriasActivas() {
        return categoriaService.findAllActivos();
    }

    // Buscar por nombre
    @GetMapping("/buscar")
    public List<CategoriaResponseDTO> buscarPorNombreCliente(@RequestParam String nombre) {
        return categoriaService.findByNameContainingIgnoreCaseCliente(nombre);
    }

    // Buscar por ID (solo ACTIVOS)
    @GetMapping("/{id}")
    public CategoriaResponseDTO obtenerPorIdCliente(@PathVariable Long id) {
        return categoriaService.findByIdCliente(id);
    }

    // ADMIN: Listar todas las categorías (incluyendo INACTIVAS)
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public List<CategoriaResponseDTO> listarCategorias() {
        return categoriaService.findAll();
    }

    // ADMIN: Filtrar por estado (ACTIVO / INACTIVO)
    @GetMapping("/admin/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public List<CategoriaResponseDTO> listarPorEstado(@RequestParam EstadoCategoria estado) {
        return categoriaService.findByEstado(estado);
    }

    // ADMIN: Buscar por nombre (incluye INACTIVAS)
    @GetMapping("/admin/buscar")
    @PreAuthorize("hasRole('ADMIN')")
    public List<CategoriaResponseDTO> buscarPorNombreAdmin(@RequestParam String nombre) {
        return categoriaService.findByNameContainingIgnoreCaseAdmin(nombre);
    }

    // ADMIN: Buscar por ID (incluye INACTIVAS)
    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoriaResponseDTO obtenerPorId(@PathVariable Long id) {
        return categoriaService.findById(id);
    }

    // ADMIN: Crear categoría (solo admin)
    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoriaResponseDTO registrarCategoria(@RequestBody CategoriaCreateDTO dto) {
        return categoriaService.create(dto);
    }

    // ADMIN: Editar categoría (solo admin)
    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoriaResponseDTO editarCategoria(
            @PathVariable Long id,
            @RequestBody CategoriaUpdateDTO dto
    ) {
        return categoriaService.update(id, dto);
    }

    // ADMIN: Desactivar categoría (y descontinuar productos)
    @PutMapping("/admin/{id}/desactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public void desactivarCategoria(@PathVariable Long id) {
        categoriaService.desactivar(id);
    }

    // ADMIN: Activar categoría
    @PutMapping("/admin/{id}/activar")
    @PreAuthorize("hasRole('ADMIN')")
    public void activarCategoria(@PathVariable Long id) {
        categoriaService.activar(id);
    }

    // ADMIN: Eliminar categoría (solo admin)
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminarCategoria(@PathVariable Long id) {
        categoriaService.delete(id);
    }
}
