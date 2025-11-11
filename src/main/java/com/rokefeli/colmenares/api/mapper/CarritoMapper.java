package com.rokefeli.colmenares.api.mapper;

import com.rokefeli.colmenares.api.dto.response.CarritoResponseDTO;
import com.rokefeli.colmenares.api.entity.Carrito;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarritoMapper {

    @Mapping(source = "usuario.id", target = "idUsuario")
    CarritoResponseDTO toResponseDTO(Carrito entity);

}
