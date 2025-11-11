package com.rokefeli.colmenares.api.mapper;

import com.rokefeli.colmenares.api.dto.create.DetalleCarritoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.DetalleCarritoResponseDTO;
import com.rokefeli.colmenares.api.entity.DetalleCarrito;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DetalleCarritoMapper {

    DetalleCarrito toEntity(DetalleCarritoCreateDTO dto);

    @Mapping(source = "producto.id", target = "idProducto")
    @Mapping(source = "producto.nombre", target = "nombreProducto")
    DetalleCarritoResponseDTO toResponseDTO(DetalleCarrito entity);

}
