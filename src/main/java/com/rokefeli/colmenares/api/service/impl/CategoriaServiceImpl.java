package com.rokefeli.colmenares.api.service.impl;

import com.rokefeli.colmenares.api.dto.create.CategoriaCreateDTO;
import com.rokefeli.colmenares.api.dto.response.CategoriaResponseDTO;
import com.rokefeli.colmenares.api.dto.update.CategoriaUpdateDTO;
import com.rokefeli.colmenares.api.entity.Categoria;
import com.rokefeli.colmenares.api.entity.enums.EstadoCategoria;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.CategoriaMapper;
import com.rokefeli.colmenares.api.repository.CategoriaRepository;
import com.rokefeli.colmenares.api.service.interfaces.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    @Autowired
    private CategoriaMapper mapper;

    @Override
    public List<CategoriaResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<CategoriaResponseDTO> findByEstado(EstadoCategoria estado) {
        return repository.findByEstado(estado)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public CategoriaResponseDTO findById(Long id) {
        Categoria existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
        return mapper.toResponseDTO(existing);
    }

    @Override
    public List<CategoriaResponseDTO> findByNameContainingIgnoreCase(String name) {
        return repository.findByNombreContainingIgnoreCase(name)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public CategoriaResponseDTO create(CategoriaCreateDTO createDTO) {
        if (repository.existsByNombreIgnoreCase(createDTO.getNombre())) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre.");
        }
        Categoria categoria = mapper.toEntity(createDTO);
        categoria.setEstado(EstadoCategoria.ACTIVO);
        Categoria saved = repository.save(categoria);
        return mapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public CategoriaResponseDTO update(Long id, CategoriaUpdateDTO updateDTO) {
        Categoria existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
        mapper.updateEntityFromDTO(updateDTO, existing);
        Categoria updated = repository.save(existing);
        return mapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void desactivar(Long id) {
        Categoria existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
        existing.setEstado(EstadoCategoria.INACTIVO);
        repository.save(existing);
    }

    @Override
    @Transactional
    public void activar(Long id) {
        Categoria existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
        existing.setEstado(EstadoCategoria.ACTIVO);
        repository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));

        repository.deleteById(id);
    }
}
