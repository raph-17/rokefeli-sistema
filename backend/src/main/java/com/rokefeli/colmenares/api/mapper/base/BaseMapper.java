package com.rokefeli.colmenares.api.mapper.base;

import org.mapstruct.Named;

public interface BaseMapper {

    // Lógica compartida para limpiar Strings
    default String trimString(String value) {
        return value == null ? null : value.trim();
    }

}