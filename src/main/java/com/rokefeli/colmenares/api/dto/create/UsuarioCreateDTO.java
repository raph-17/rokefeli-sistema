package com.rokefeli.colmenares.api.dto.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UsuarioCreateDTO {
    @NotBlank
    private String nombres;
    @NotBlank
    private String apellidos;
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")
    private String dni;
    @Email
    private String email;
    @NotBlank
    private String password;
}
