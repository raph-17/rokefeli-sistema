package com.rokefeli.colmenares.api.controller;

import com.rokefeli.colmenares.api.dto.create.DepartamentoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.DepartamentoResponseDTO;
import com.rokefeli.colmenares.api.service.interfaces.DepartamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departamentos")
@RequiredArgsConstructor
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    // Listar departamentos
    @GetMapping
    public List<DepartamentoResponseDTO> listarDepartamentos() {
        return departamentoService.findAll();
    }

    // Registrar departamento
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DepartamentoResponseDTO registrarDepartamento(@RequestBody DepartamentoCreateDTO dto) {
        return departamentoService.create(dto);
    }
}