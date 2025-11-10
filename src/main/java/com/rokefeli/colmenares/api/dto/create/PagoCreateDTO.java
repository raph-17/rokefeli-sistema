package com.rokefeli.colmenares.api.dto.create;

import com.rokefeli.colmenares.api.entity.enums.MetodoPago;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PagoCreateDTO {

    @NotNull
    private Long idVenta;

    @NotNull
    private MetodoPago metodoPago;

    @NotNull
    private BigDecimal monto;

    private String referenciaPasarela;
}
