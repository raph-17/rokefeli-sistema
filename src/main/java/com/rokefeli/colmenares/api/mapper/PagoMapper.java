package com.rokefeli.colmenares.api.mapper;

import com.rokefeli.colmenares.api.dto.create.PagoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.PagoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.PagoUpdateDTO;
import com.rokefeli.colmenares.api.entity.Pago;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PagoMapper {

    Pago toEntity(PagoCreateDTO dto);

    @Mapping(source = "venta.id", target = "idVenta")
    PagoResponseDTO toResponseDTO(Pago entity);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(PagoUpdateDTO dto, @MappingTarget Pago entity);

}
