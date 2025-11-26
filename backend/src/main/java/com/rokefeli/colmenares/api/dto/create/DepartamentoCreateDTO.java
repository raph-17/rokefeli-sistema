package com.rokefeli.colmenares.api.dto.create;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DepartamentoCreateDTO {
    @NotBlank
    private String nombre;
}
