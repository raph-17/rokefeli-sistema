package com.rokefeli.colmenares.api.controller;

import com.rokefeli.colmenares.api.dto.create.AgenciaEnvioCreateDTO;
import com.rokefeli.colmenares.api.dto.create.DepartamentoCreateDTO;
import com.rokefeli.colmenares.api.dto.create.TarifaEnvioCreateDTO;
import com.rokefeli.colmenares.api.dto.response.AgenciaEnvioResponseDTO;
import com.rokefeli.colmenares.api.dto.response.DepartamentoResponseDTO;
import com.rokefeli.colmenares.api.dto.response.TarifaEnvioResponseDTO;
import com.rokefeli.colmenares.api.service.interfaces.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/api/v1/logistica") // Renombrado a logistica para agrupar
@RequiredArgsConstructor
public class UbicacionController {

    private final DepartamentoService departamentoService;
    private final ProvinciaService provinciaService;
    private final DistritoService distritoService;
    private final AgenciaEnvioService agenciaEnvioService;
    private final TarifaEnvioService tarifaEnvioService;

    // --- GESTIÓN DE UBICACIONES (ADMIN) ---

    @PostMapping("/departamentos")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"ROLE_ADMIN", "ROLE_EMPLEADO"})
    public DepartamentoResponseDTO crearDepartamento(@RequestBody DepartamentoCreateDTO dto) {
        return departamentoService.create(dto);
    }

    @GetMapping("/departamentos")
    public List<DepartamentoResponseDTO> listarDepartamentos() {
        return departamentoService.findAll();
    }

    // --- GESTIÓN DE TARIFAS Y AGENCIAS ---

    @PostMapping("/agencias")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"ROLE_ADMIN", "ROLE_EMPLEADO"})
    public AgenciaEnvioResponseDTO crearAgencia(@RequestBody AgenciaEnvioCreateDTO dto) {
        return agenciaEnvioService.create(dto);
    }

    @PostMapping("/tarifas")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"ROLE_ADMIN", "ROLE_EMPLEADO"})
    public TarifaEnvioResponseDTO crearTarifa(@RequestBody TarifaEnvioCreateDTO dto) {
        return tarifaEnvioService.create(dto);
    }

    // Listar tarifas por distrito (PÚBLICO)
    @GetMapping("/tarifas/distrito/{idDistrito}")
    public List<TarifaEnvioResponseDTO> listarTarifasPorDistrito(@PathVariable Long idDistrito) {
        // Asumo un método en el servicio para obtener tarifas por distrito
        return tarifaEnvioService.findByDistritoId(idDistrito);
    }
}