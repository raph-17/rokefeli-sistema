package com.rokefeli.colmenares.api.dto.update;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AgenciaEnvioUpdateDTO {
    @NotBlank
    private String nombre;
}
