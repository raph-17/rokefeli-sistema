package com.rokefeli.colmenares.api.service.interfaces;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.AgenciaEnvioCreateDTO;
import com.rokefeli.colmenares.api.dto.response.AgenciaEnvioResponseDTO;
import com.rokefeli.colmenares.api.dto.update.AgenciaEnvioUpdateDTO;

public interface AgenciaEnvioService {
    List<AgenciaEnvioResponseDTO> findAll();
    AgenciaEnvioResponseDTO findById(Long id);
    AgenciaEnvioResponseDTO create(AgenciaEnvioCreateDTO createDTO);
    AgenciaEnvioResponseDTO update(Long id, AgenciaEnvioUpdateDTO updateDTO);
    void softDelete(Long id);
    void hardDelete(Long id);
}
