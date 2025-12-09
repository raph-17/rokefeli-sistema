package com.rokefeli.colmenares.api.mapper.base;

public interface BaseMapper {

    // Lógica compartida para limpiar Strings
    default String trimString(String value) {
        return value == null ? null : value.trim();
    }

}