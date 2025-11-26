package com.rokefeli.colmenares.api.dto.create;

import jakarta.validation.constraints.Null;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
public class VentaInternoCreateDTO {

    @NotNull
    private Long idEmpleado;

    @Null
    private Long idDistribuidor; // OPCIONAL

    @NotNull(message = "Debe incluir al menos un producto en la venta")
    private List<DetalleVentaCreateDTO> detalles;
}