package com.rokefeli.colmenares.api.dto.response;

import com.rokefeli.colmenares.api.entity.enums.EstadoDistrito;
import lombok.Data;

@Data
public class DistritoResponseDTO {
    private Long id;
    private Long idProvincia;
    private String nombre;
    private EstadoDistrito estado;
}
