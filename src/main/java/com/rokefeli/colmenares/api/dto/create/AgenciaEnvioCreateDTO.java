package com.rokefeli.colmenares.api.dto.create;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AgenciaEnvioCreateDTO {
    @NotBlank
    private String nombre;
}
