package com.rokefeli.colmenares.api.dto.create;

import com.rokefeli.colmenares.api.entity.enums.Rol;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdminCreateDTO extends UsuarioCreateDTO {
    @NotNull(message = "El rol no puede ser nulo")
    private Rol rol;
}
