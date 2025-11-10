package com.rokefeli.colmenares.api.dto.update;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockAdjustmentDTO {
    @NotNull
    private Long productoId;
    @NotNull
    private Integer cantidadCambio;
}
