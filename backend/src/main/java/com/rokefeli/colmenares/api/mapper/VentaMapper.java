package com.rokefeli.colmenares.api.mapper;

import com.rokefeli.colmenares.api.dto.create.VentaOnlineCreateDTO;
import com.rokefeli.colmenares.api.dto.response.VentaResponseDTO;
import com.rokefeli.colmenares.api.entity.Venta;
import com.rokefeli.colmenares.api.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VentaMapper extends BaseMapper {

    @Mapping(target = "detalles", ignore = true)
    Venta toEntity(VentaOnlineCreateDTO dto);

    @Mapping(source = "usuario.id", target = "idUsuario")
    @Mapping(source = "usuario.nombres", target = "nombreUsuario")
    VentaResponseDTO toResponseDTO(Venta entity);

}
