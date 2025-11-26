package com.rokefeli.colmenares.api.dto.create;

import com.rokefeli.colmenares.api.entity.enums.ModalidadEntrega;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PedidoCreateDTO {
    @NotNull
    private Long idVenta;
    @NotNull(message = "El distrito es obligatorio")
    private Long idDistrito;
    @NotNull(message = "La agencia de envío es obligatoria")
    private Long idAgenciaEnvio;
    @NotBlank(message = "La dirección de envío es obligatoria")
    private String direccionEnvio;
    @NotNull
    private ModalidadEntrega modalidadEntrega;
    private String referenciaCliente;
}
