package com.rokefeli.colmenares.api.mapper;

import com.rokefeli.colmenares.api.dto.create.AdminCreateDTO;
import com.rokefeli.colmenares.api.dto.create.UsuarioCreateDTO;
import com.rokefeli.colmenares.api.dto.response.UsuarioResponseDTO;
import com.rokefeli.colmenares.api.dto.update.AdminUpdateDTO;
import com.rokefeli.colmenares.api.dto.update.UsuarioUpdateDTO;
import com.rokefeli.colmenares.api.entity.Usuario;
import com.rokefeli.colmenares.api.mapper.base.BaseMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsuarioMapper extends BaseMapper {

    Usuario toEntity(UsuarioCreateDTO dto);

    Usuario toEntityFromAdminCreateDTO(AdminCreateDTO dto);

    UsuarioResponseDTO toResponseDTO(Usuario entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(UsuarioUpdateDTO dto, @MappingTarget Usuario entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromAdminUpdateDTO(AdminUpdateDTO dto, @MappingTarget Usuario entity);

}
