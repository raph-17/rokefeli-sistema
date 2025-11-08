package com.rokefeli.colmenares.api.dto.update;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordChangeDTO {
    @NotBlank
    private String actualPassword;
    @NotBlank
    private String nuevaPassword;
}
