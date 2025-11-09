package com.rokefeli.colmenares.api.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoriaResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaRegistro;
}
