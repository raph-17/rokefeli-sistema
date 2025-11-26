package com.rokefeli.colmenares.api.dto.create;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoriaCreateDTO {
    @NotBlank
    private String nombre;
    @NotBlank
    private String descripcion;
}
