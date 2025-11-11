package com.rokefeli.colmenares.api.mapper;

import com.rokefeli.colmenares.api.dto.create.DistritoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.DistritoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.DistritoUpdateDTO;
import com.rokefeli.colmenares.api.entity.Distrito;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DistritoMapper {

    Distrito toEntity(DistritoCreateDTO dto);

    @Mapping(source = "provincia.id", target = "idProvincia")
    DistritoResponseDTO toResponseDTO(Distrito entity);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(DistritoUpdateDTO dto, @MappingTarget Distrito entity);

}
