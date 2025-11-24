package com.rokefeli.colmenares.api.mapper;

import com.rokefeli.colmenares.api.dto.response.CarritoResponseDTO;
import com.rokefeli.colmenares.api.entity.Carrito;
import com.rokefeli.colmenares.api.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CarritoMapper extends BaseMapper {

    @Mapping(source = "usuario.id", target = "idUsuario")
    CarritoResponseDTO toResponseDTO(Carrito entity);

}
