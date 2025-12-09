package com.rokefeli.colmenares.api.dto.create;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class VentaOnlineCreateDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;

    @NotNull(message = "Debe incluir al menos un producto en la venta")
    private List<DetalleVentaCreateDTO> detalles;
}
