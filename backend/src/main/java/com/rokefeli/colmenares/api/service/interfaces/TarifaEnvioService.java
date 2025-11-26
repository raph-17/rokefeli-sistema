package com.rokefeli.colmenares.api.service.interfaces;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.TarifaEnvioCreateDTO;
import com.rokefeli.colmenares.api.dto.response.TarifaEnvioResponseDTO;
import com.rokefeli.colmenares.api.dto.update.TarifaEnvioUpdateDTO;

public interface TarifaEnvioService {
    List<TarifaEnvioResponseDTO> findAll();
    TarifaEnvioResponseDTO findById(Long id);
    TarifaEnvioResponseDTO create(TarifaEnvioCreateDTO createDTO);
    TarifaEnvioResponseDTO update(Long id, TarifaEnvioUpdateDTO updateDTO);
    void delete(Long id);
    void desactivar(Long id);
    void activar(Long id);
    List<TarifaEnvioResponseDTO> findByDistritoId(Long idDistrito);
    List<TarifaEnvioResponseDTO> findByAgenciaId(Long idAgencia);
    TarifaEnvioResponseDTO findByAgenciaIdAndDistritoId(Long idAgencia, Long idDistrito);
    List<TarifaEnvioResponseDTO> findByDistritoIdActivo(Long idDistrito);
}
