package com.rokefeli.colmenares.api.dto.response;

import com.rokefeli.colmenares.api.entity.enums.EstadoUsuario;
import com.rokefeli.colmenares.api.entity.enums.Rol;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsuarioResponseDTO {
    private Long id;
    private String nombres;
    private String apellidos;
    private String dni;
    private Rol rol;
    private String telefono;
    private String email;
    private EstadoUsuario estado;
    private LocalDateTime fechaRegistro;
}
