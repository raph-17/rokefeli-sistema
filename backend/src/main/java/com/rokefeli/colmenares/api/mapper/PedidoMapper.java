package com.rokefeli.colmenares.api.mapper;

import com.rokefeli.colmenares.api.dto.create.PedidoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.PedidoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.PedidoUpdateDTO;
import com.rokefeli.colmenares.api.entity.Pedido;
import com.rokefeli.colmenares.api.mapper.base.BaseMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PedidoMapper extends BaseMapper {

    Pedido toEntity(PedidoCreateDTO dto);

    @Mapping(source = "venta.id", target = "idVenta")
    @Mapping(source = "distrito.id", target = "idDistrito")
    @Mapping(source = "distrito.nombre", target = "nombreDistrito")
    @Mapping(source = "agenciaEnvio.id", target = "idAgenciaEnvio")
    @Mapping(source = "agenciaEnvio.nombre", target = "nombreAgencia")
    PedidoResponseDTO toResponseDTO(Pedido entity);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(PedidoUpdateDTO dto, @MappingTarget Pedido entity);
}
