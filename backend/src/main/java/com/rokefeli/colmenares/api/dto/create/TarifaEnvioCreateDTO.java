package com.rokefeli.colmenares.api.dto.create;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TarifaEnvioCreateDTO {
    @NotNull
    private Long idAgenciaEnvio;
    @NotNull
    private Long idDistrito;
    @NotNull
    private BigDecimal costoEnvio;
    @NotNull
    private Integer diasEstimados;
}
