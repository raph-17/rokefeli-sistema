package com.rokefeli.colmenares.api.mapper;

import com.rokefeli.colmenares.api.dto.create.PagoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.PagoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.PagoUpdateDTO;
import com.rokefeli.colmenares.api.entity.Pago;
import com.rokefeli.colmenares.api.mapper.base.BaseMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PagoMapper extends BaseMapper {

    Pago toEntity(PagoCreateDTO dto);

    @Mapping(source = "venta.id", target = "idVenta")
    PagoResponseDTO toResponseDTO(Pago entity);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(PagoUpdateDTO dto, @MappingTarget Pago entity);

}
