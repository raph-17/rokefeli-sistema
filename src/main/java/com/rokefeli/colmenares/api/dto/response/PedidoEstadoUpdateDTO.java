package com.rokefeli.colmenares.api.dto.response;

import com.rokefeli.colmenares.api.entity.enums.EstadoPedido;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PedidoEstadoUpdateDTO {
    @NotNull
    private EstadoPedido estado;
}
