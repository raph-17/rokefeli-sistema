package com.rokefeli.colmenares.api.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoCreateDTO {
    @NotBlank
    private String nombre;
    @NotBlank
    private String descripcion;
    @NotNull(message = "La categoría es obligatoria")
    private Long idCategoria;
    @NotNull
    private BigDecimal precio;
    @NotNull
    private Integer stockActual;
    @NotNull
    private Integer stockMinimo;
    @NotBlank
    private String imagenUrl;
}
