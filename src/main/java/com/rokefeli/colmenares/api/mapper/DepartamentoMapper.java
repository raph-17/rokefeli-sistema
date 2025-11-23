package com.rokefeli.colmenares.api.mapper;

import com.rokefeli.colmenares.api.dto.create.DepartamentoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.DepartamentoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.DepartamentoUpdateDTO;
import com.rokefeli.colmenares.api.entity.Departamento;
import com.rokefeli.colmenares.api.mapper.base.BaseMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DepartamentoMapper extends BaseMapper {

    Departamento toEntity(DepartamentoCreateDTO dto);

    DepartamentoResponseDTO toResponseDTO(Departamento entity);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(DepartamentoUpdateDTO dto, @MappingTarget Departamento entity);
}
