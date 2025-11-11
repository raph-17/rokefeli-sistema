package com.rokefeli.colmenares.api.mapper;

import com.rokefeli.colmenares.api.dto.create.ProductoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.ProductoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.ProductoUpdateDTO;
import com.rokefeli.colmenares.api.entity.Producto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    Producto toEntity(ProductoCreateDTO dto);

    @Mapping(source = "categoria.nombre", target = "categoriaNombre")
    @Mapping(source = "categoria.id", target = "categoriaId")
    ProductoResponseDTO toResponseDTO(Producto entity);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(ProductoUpdateDTO dto, @MappingTarget Producto entity);

}
