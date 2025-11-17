package com.rokefeli.colmenares.api.mapper;

import com.rokefeli.colmenares.api.dto.create.VentaCreateDTO;
import com.rokefeli.colmenares.api.dto.response.VentaResponseDTO;
import com.rokefeli.colmenares.api.entity.Venta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VentaMapper {

    @Mapping(target = "detalles", ignore = true)
    Venta toEntity(VentaCreateDTO dto);

    @Mapping(source = "usuario.id", target = "idUsuario")
    @Mapping(source = "usuario.nombres", target = "nombreUsuario")
    VentaResponseDTO toResponseDTO(Venta entity);

}
