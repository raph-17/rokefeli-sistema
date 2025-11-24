package com.rokefeli.colmenares.api.mapper;

import com.rokefeli.colmenares.api.dto.create.DetalleCarritoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.DetalleCarritoResponseDTO;
import com.rokefeli.colmenares.api.entity.DetalleCarrito;
import com.rokefeli.colmenares.api.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DetalleCarritoMapper extends BaseMapper {

    DetalleCarrito toEntity(DetalleCarritoCreateDTO dto);

    @Mapping(source = "producto.id", target = "idProducto")
    @Mapping(source = "producto.nombre", target = "nombreProducto")
    DetalleCarritoResponseDTO toResponseDTO(DetalleCarrito entity);

}
