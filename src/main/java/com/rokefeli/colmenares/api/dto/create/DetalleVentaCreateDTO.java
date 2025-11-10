package com.rokefeli.colmenares.api.dto.create;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class DetalleVentaCreateDTO {

    @NotNull
    private Long idProducto;

    @NotNull
    private Integer cantidad;
}
