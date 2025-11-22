package com.rokefeli.colmenares.api.controller;

import com.rokefeli.colmenares.api.dto.create.DistritoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.DistritoResponseDTO;
import com.rokefeli.colmenares.api.service.interfaces.DistritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/distritos")
@RequiredArgsConstructor
public class DistritoController {

    private final DistritoService distritoService;

    // Listar distritos
    @GetMapping
    public List<DistritoResponseDTO> listarDistritos() {
        return distritoService.findAll();
    }

    // Registrar distrito
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DistritoResponseDTO registrarDistrito(@RequestBody DistritoCreateDTO dto) {
        return distritoService.create(dto);
    }
}