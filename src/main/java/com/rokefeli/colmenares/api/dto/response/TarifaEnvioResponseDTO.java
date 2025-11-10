package com.rokefeli.colmenares.api.dto.response;

import com.rokefeli.colmenares.api.entity.enums.EstadoTarifa;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TarifaEnvioResponseDTO {
    private Long id;
    private Long idAgenciaEnvio;
    private String nombreAgenciaEnvio;
    private Long idDistrito;
    private String nombreDistrito;
    private String provinciaDistrito;
    private String departamentoDistrito;
    private BigDecimal costoEnvio;
    private Integer diasEstimados;
    private EstadoTarifa estado;
}
