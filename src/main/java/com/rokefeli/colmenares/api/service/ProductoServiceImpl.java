package com.rokefeli.colmenares.api.service;

import com.rokefeli.colmenares.api.dto.create.ProductoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.ProductoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.ProductoUpdateDTO;
import com.rokefeli.colmenares.api.entity.Producto;
import com.rokefeli.colmenares.api.entity.enums.EstadoProducto;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.ProductoMapper;
import com.rokefeli.colmenares.api.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository repository;

    @Autowired
    private ProductoMapper mapper;

    @Override
    public List<ProductoResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public ProductoResponseDTO findById(Long id) {
        Producto existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
        return mapper.toResponseDTO(existing);
    }

    @Override
    public ProductoResponseDTO create(ProductoCreateDTO createDTO) {
        Producto producto = mapper.toEntity(createDTO);
        Producto saved = repository.save(producto);
        return mapper.toResponseDTO(saved);
    }

    @Override
    public ProductoResponseDTO update(Long id, ProductoUpdateDTO updateDTO) {
        Producto existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
        mapper.updateEntityFromDTO(updateDTO, existing);
        Producto updated = repository.save(existing);
        return mapper.toResponseDTO(updated);
    }

    @Override
    public void softDelete(Long id) {
        Producto existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
        existing.setEstado(EstadoProducto.DESCONTINUADO);
        repository.save(existing);
    }

    @Override
    public void hardDelete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Producto", id);
        }
        repository.deleteById(id);
    }
}
