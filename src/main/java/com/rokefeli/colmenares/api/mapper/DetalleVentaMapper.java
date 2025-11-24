package com.rokefeli.colmenares.api.mapper;

import com.rokefeli.colmenares.api.dto.create.DetalleVentaCreateDTO;
import com.rokefeli.colmenares.api.dto.response.DetalleVentaResponseDTO;
import com.rokefeli.colmenares.api.entity.DetalleVenta;
import com.rokefeli.colmenares.api.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DetalleVentaMapper extends BaseMapper {

    DetalleVenta toEntity(DetalleVentaCreateDTO dto);

    @Mapping(source = "producto.id", target = "idProducto")
    @Mapping(source = "producto.nombre", target = "nombreProducto")
    DetalleVentaResponseDTO toResponseDTO(DetalleVenta entity);

}
