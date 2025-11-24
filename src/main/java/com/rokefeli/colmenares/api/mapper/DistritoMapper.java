package com.rokefeli.colmenares.api.mapper;

import com.rokefeli.colmenares.api.dto.create.DistritoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.DistritoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.DistritoUpdateDTO;
import com.rokefeli.colmenares.api.entity.Distrito;
import com.rokefeli.colmenares.api.mapper.base.BaseMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DistritoMapper extends BaseMapper {

    Distrito toEntity(DistritoCreateDTO dto);

    @Mapping(source = "provincia.id", target = "idProvincia")
    DistritoResponseDTO toResponseDTO(Distrito entity);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(DistritoUpdateDTO dto, @MappingTarget Distrito entity);

}
