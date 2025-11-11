package com.rokefeli.colmenares.api.mapper;

import com.rokefeli.colmenares.api.dto.create.UsuarioCreateDTO;
import com.rokefeli.colmenares.api.dto.response.UsuarioResponseDTO;
import com.rokefeli.colmenares.api.dto.update.UsuarioUpdateDTO;
import com.rokefeli.colmenares.api.entity.Usuario;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toEntity(UsuarioCreateDTO dto);

    UsuarioResponseDTO toResponseDTO(Usuario entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(UsuarioUpdateDTO dto, @MappingTarget Usuario entity);
}
