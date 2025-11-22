package com.rokefeli.colmenares.api.controller;

import com.rokefeli.colmenares.api.dto.create.CategoriaCreateDTO;
import com.rokefeli.colmenares.api.dto.response.CategoriaResponseDTO;
import com.rokefeli.colmenares.api.service.interfaces.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    // Listar categorías
    @GetMapping
    public List<CategoriaResponseDTO> listarCategorias() {
        return categoriaService.findAll();
    }

    // Registrar categoría
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaResponseDTO registrarCategoria(@RequestBody CategoriaCreateDTO dto) {
        return categoriaService.create(dto);
    }
}