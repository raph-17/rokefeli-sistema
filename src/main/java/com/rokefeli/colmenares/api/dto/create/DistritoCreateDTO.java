package com.rokefeli.colmenares.api.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DistritoCreateDTO {
    @NotNull(message = "La provincia es obligatoria")
    private Long idProvincia;
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
}
