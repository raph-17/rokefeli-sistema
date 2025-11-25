package com.rokefeli.colmenares.api.dto.response;

import com.rokefeli.colmenares.api.entity.enums.EstadoProvincia;
import lombok.Data;

@Data
public class ProvinciaResponseDTO {
    private Long id;
    private Long idDepartamento;
    private String nombre;
    private EstadoProvincia estado;
}
