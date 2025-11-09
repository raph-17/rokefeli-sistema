package com.rokefeli.colmenares.api.dto.update;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DetalleCarritoUpdateDTO {
    @NotNull
    private Long idProducto;

    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer cantidad;
}
