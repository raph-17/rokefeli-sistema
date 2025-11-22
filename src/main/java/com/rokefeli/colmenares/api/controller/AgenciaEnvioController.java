package com.rokefeli.colmenares.api.controller;

import com.rokefeli.colmenares.api.dto.create.AgenciaEnvioCreateDTO;
import com.rokefeli.colmenares.api.dto.response.AgenciaEnvioResponseDTO;
import com.rokefeli.colmenares.api.service.interfaces.AgenciaEnvioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/agencias")
@RequiredArgsConstructor
public class AgenciaEnvioController {

    private final AgenciaEnvioService agenciaService;

    // Listar agencias
    @GetMapping
    public List<AgenciaEnvioResponseDTO> listarAgencias() {
        return agenciaService.findAll();
    }

    // Registrar agencia
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AgenciaEnvioResponseDTO registrarAgencia(@RequestBody AgenciaEnvioCreateDTO dto) {
        return agenciaService.create(dto);
    }
}