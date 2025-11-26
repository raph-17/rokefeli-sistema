package com.rokefeli.colmenares.api.dto.response;

import com.rokefeli.colmenares.api.entity.enums.EstadoAgencia;
import lombok.Data;

@Data
public class AgenciaEnvioResponseDTO {
    private Long id;
    private String nombre;
    private EstadoAgencia estado;
}
