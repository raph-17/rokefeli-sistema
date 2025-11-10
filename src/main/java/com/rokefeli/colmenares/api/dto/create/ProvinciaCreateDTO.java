package com.rokefeli.colmenares.api.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProvinciaCreateDTO {
    @NotNull(message = "El departamento es obligatorio")
    private Long idDepartamento;
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
}
