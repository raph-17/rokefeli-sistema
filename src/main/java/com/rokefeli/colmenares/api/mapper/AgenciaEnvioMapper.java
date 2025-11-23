package com.rokefeli.colmenares.api.mapper;

import com.rokefeli.colmenares.api.dto.create.AgenciaEnvioCreateDTO;
import com.rokefeli.colmenares.api.dto.response.AgenciaEnvioResponseDTO;
import com.rokefeli.colmenares.api.dto.update.AgenciaEnvioUpdateDTO;
import com.rokefeli.colmenares.api.entity.AgenciaEnvio;
import com.rokefeli.colmenares.api.mapper.base.BaseMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AgenciaEnvioMapper extends BaseMapper {

    AgenciaEnvio toEntity(AgenciaEnvioCreateDTO dto);

    AgenciaEnvioResponseDTO toResponseDTO(AgenciaEnvio entity);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(AgenciaEnvioUpdateDTO dto, @MappingTarget AgenciaEnvio entity);
}
