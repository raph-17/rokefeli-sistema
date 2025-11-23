package com.rokefeli.colmenares.api.mapper;

import com.rokefeli.colmenares.api.dto.create.TarifaEnvioCreateDTO;
import com.rokefeli.colmenares.api.dto.response.TarifaEnvioResponseDTO;
import com.rokefeli.colmenares.api.dto.update.TarifaEnvioUpdateDTO;
import com.rokefeli.colmenares.api.entity.TarifaEnvio;
import com.rokefeli.colmenares.api.mapper.base.BaseMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TarifaEnvioMapper extends BaseMapper {
    
    TarifaEnvio toEntity(TarifaEnvioCreateDTO dto);

    @Mapping(source = "agenciaEnvio.id", target = "idAgenciaEnvio")
    @Mapping(source = "agenciaEnvio.nombre", target = "nombreAgenciaEnvio")
    @Mapping(source = "distrito.id", target = "idDistrito")
    @Mapping(source = "distrito.nombre", target = "nombreDistrito")
    @Mapping(source = "distrito.provincia.nombre", target = "provinciaDistrito")
    @Mapping(source = "distrito.provincia.departamento.nombre", target = "departamentoDistrito")
    TarifaEnvioResponseDTO toResponseDTO(TarifaEnvio entity);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(TarifaEnvioUpdateDTO dto, @MappingTarget TarifaEnvio entity);
    
}
