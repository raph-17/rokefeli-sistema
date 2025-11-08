package com.rokefeli.colmenares.api.dto.update;

import com.rokefeli.colmenares.api.entity.enums.EstadoAgencia;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AgenciaEnvioUpdateDTO {
    @NotBlank
    private String nombre;
    @NotNull
    private EstadoAgencia estado;
}
