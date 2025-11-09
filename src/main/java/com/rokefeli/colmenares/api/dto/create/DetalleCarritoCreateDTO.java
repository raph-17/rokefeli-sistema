package com.rokefeli.colmenares.api.dto.create;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DetalleCarritoCreateDTO {
    @NotNull
    private Long idProducto;

    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;
}
