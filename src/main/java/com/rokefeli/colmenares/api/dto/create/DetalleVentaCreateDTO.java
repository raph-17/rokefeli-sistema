package com.rokefeli.colmenares.api.dto.create;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DetalleVentaCreateDTO {

    @NotNull
    private Long idProducto;

    @NotNull
    private Integer cantidad;
}
