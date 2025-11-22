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

    // 🔒 Listar todas las categorías (incluyendo INACTIVAS)
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public List<CategoriaResponseDTO> listarCategorias() {
        return categoriaService.findAll();
    }

    // ✅ Listar todas las categorías ACTIVAS (para clientes)
    @GetMapping
    public List<CategoriaResponseDTO> listarCategoriasActivas() {
        return categoriaService.findAllActivos();
    }

    // 🔒 Filtrar por estado (ACTIVO / INACTIVO)
    @GetMapping("/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public List<CategoriaResponseDTO> listarPorEstado(@RequestParam EstadoCategoria estado) {
        return categoriaService.findByEstado(estado);
    }

    // ✅ Buscar por nombre (PÚBLICO)
    @GetMapping("/buscar")
    public List<CategoriaResponseDTO> buscarPorNombreCliente(@RequestParam String nombre) {
        return categoriaService.findByNameContainingIgnoreCaseCliente(nombre);
    }

    // 🔒 Buscar por nombre (incluye INACTIVAS)
    @GetMapping("/admin/buscar")
    @PreAuthorize("hasRole('ADMIN')")
    public List<CategoriaResponseDTO> buscarPorNombreAdmin(@RequestParam String nombre) {
        return categoriaService.findByNameContainingIgnoreCaseAdmin(nombre);
    }

    // ✅ Buscar por ID (solo ACTIVOS)
    @GetMapping("/{id}")
    public CategoriaResponseDTO obtenerPorIdCliente(@PathVariable Long id) {
        return categoriaService.findByIdCliente(id);
    }

    // 🔒 Buscar por ID (incluye INACTIVAS)
    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoriaResponseDTO obtenerPorId(@PathVariable Long id) {
        return categoriaService.findById(id);
    }

    // 🔒 Crear categoría (solo admin)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CategoriaResponseDTO registrarCategoria(@RequestBody CategoriaCreateDTO dto) {
        return categoriaService.create(dto);
    }

    // 🔒 Editar categoría (solo admin)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoriaResponseDTO editarCategoria(
            @PathVariable Long id,
            @RequestBody CategoriaUpdateDTO dto
    ) {
        return categoriaService.update(id, dto);
    }

    // 🔒 Desactivar categoría (y descontinuar productos)
    @PutMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public void desactivarCategoria(@PathVariable Long id) {
        categoriaService.desactivar(id);
    }

    // 🔒 Activar categoría
    @PutMapping("/{id}/activar")
    @PreAuthorize("hasRole('ADMIN')")
    public void activarCategoria(@PathVariable Long id) {
        categoriaService.activar(id);
    }

    // 🔒 Eliminar categoría (solo admin)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminarCategoria(@PathVariable Long id) {
        categoriaService.delete(id);
    }
}
