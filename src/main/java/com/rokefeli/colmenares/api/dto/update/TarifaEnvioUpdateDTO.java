package com.rokefeli.colmenares.api.dto.update;

import com.rokefeli.colmenares.api.entity.enums.EstadoTarifa;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TarifaEnvioUpdateDTO {
    private BigDecimal costoEnvio;
    private Integer diasEstimados;
    private EstadoTarifa estado;
}
