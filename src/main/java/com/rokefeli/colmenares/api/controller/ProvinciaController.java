package com.rokefeli.colmenares.api.controller;

import com.rokefeli.colmenares.api.dto.create.ProvinciaCreateDTO;
import com.rokefeli.colmenares.api.dto.response.ProvinciaResponseDTO;
import com.rokefeli.colmenares.api.service.interfaces.ProvinciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/provincias")
@RequiredArgsConstructor
public class ProvinciaController {

    private final ProvinciaService provinciaService;

    // Listar provincias
    @GetMapping
    public List<ProvinciaResponseDTO> listarProvincias() {
        return provinciaService.findAll();
    }

    // Registrar provincia
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProvinciaResponseDTO registrarProvincia(@RequestBody ProvinciaCreateDTO dto) {
        return provinciaService.create(dto);
    }
}