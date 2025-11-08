package com.rokefeli.colmenares.api.dto.update;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioUpdateDTO {
    @NotBlank
    private String nombres;
    @NotBlank
    private String apellidos;
    @NotBlank
    private String telefono;
    @NotBlank
    private String email;
}
