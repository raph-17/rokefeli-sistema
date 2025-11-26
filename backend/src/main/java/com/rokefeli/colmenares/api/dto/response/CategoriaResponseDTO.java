package com.rokefeli.colmenares.api.dto.response;

import com.rokefeli.colmenares.api.entity.enums.EstadoCategoria;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoriaResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private EstadoCategoria estado;
    private LocalDateTime fechaRegistro;
}
