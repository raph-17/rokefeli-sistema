package com.rokefeli.colmenares.api.controller;

import com.rokefeli.colmenares.api.dto.create.TarifaEnvioCreateDTO;
import com.rokefeli.colmenares.api.dto.response.TarifaEnvioResponseDTO;
import com.rokefeli.colmenares.api.service.interfaces.TarifaEnvioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tarifas")
@RequiredArgsConstructor
public class TarifaEnvioController {

    private final TarifaEnvioService tarifaService;

    // Crear una tarifa
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TarifaEnvioResponseDTO crearTarifa(@RequestBody TarifaEnvioCreateDTO dto) {
        return tarifaService.create(dto);
    }

    // Listar tarifas por distrito (útil para que el frontend calcule envío)
    @GetMapping("/distrito/{idDistrito}")
    public List<TarifaEnvioResponseDTO> listarPorDistrito(@PathVariable Long idDistrito) {
        return tarifaService.findByDistritoId(idDistrito);
    }
    
    // Listar todas las tarifas (para ver qué tienes configurado)
    @GetMapping
    public List<TarifaEnvioResponseDTO> listarTodas() {
        return tarifaService.findAll();
    }
}