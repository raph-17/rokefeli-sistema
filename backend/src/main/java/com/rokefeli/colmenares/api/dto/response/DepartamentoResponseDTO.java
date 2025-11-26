package com.rokefeli.colmenares.api.dto.response;

import com.rokefeli.colmenares.api.entity.enums.EstadoDepartamento;
import lombok.Data;

@Data
public class DepartamentoResponseDTO {
    private Long id;
    private String nombre;
    private EstadoDepartamento estado;
}
