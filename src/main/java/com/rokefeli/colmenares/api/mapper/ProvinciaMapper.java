package com.rokefeli.colmenares.api.mapper;

import com.rokefeli.colmenares.api.dto.create.ProvinciaCreateDTO;
import com.rokefeli.colmenares.api.dto.response.ProvinciaResponseDTO;
import com.rokefeli.colmenares.api.dto.update.ProvinciaUpdateDTO;
import com.rokefeli.colmenares.api.entity.Provincia;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProvinciaMapper {

    Provincia toEntity(ProvinciaCreateDTO dto);

    @Mapping(source = "departamento.id", target = "idDepartamento")
    ProvinciaResponseDTO toResponseDTO(Provincia entity);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(ProvinciaUpdateDTO dto, @MappingTarget Provincia entity);

}
