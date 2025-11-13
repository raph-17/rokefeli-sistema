package com.rokefeli.colmenares.api.service.interfaces;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.AgenciaEnvioCreateDTO;
import com.rokefeli.colmenares.api.dto.response.AgenciaEnvioResponseDTO;
import com.rokefeli.colmenares.api.dto.update.AgenciaEnvioUpdateDTO;
import com.rokefeli.colmenares.api.entity.enums.EstadoAgencia;

public interface AgenciaEnvioService {
    List<AgenciaEnvioResponseDTO> findAll();
    List<AgenciaEnvioResponseDTO> findByEstado(EstadoAgencia estado);
    AgenciaEnvioResponseDTO findById(Long id);
    AgenciaEnvioResponseDTO create(AgenciaEnvioCreateDTO createDTO);
    AgenciaEnvioResponseDTO update(Long id, AgenciaEnvioUpdateDTO updateDTO);
    void desactivar(Long id);
    void activar(Long id);
    void delete(Long id);
}
