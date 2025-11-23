package com.rokefeli.colmenares.api.service.impl;

import com.rokefeli.colmenares.api.dto.create.CategoriaCreateDTO;
import com.rokefeli.colmenares.api.dto.response.CategoriaResponseDTO;
import com.rokefeli.colmenares.api.dto.update.CategoriaUpdateDTO;
import com.rokefeli.colmenares.api.entity.Categoria;
import com.rokefeli.colmenares.api.entity.Producto;
import com.rokefeli.colmenares.api.entity.enums.EstadoCategoria;
import com.rokefeli.colmenares.api.entity.enums.EstadoProducto;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.CategoriaMapper;
import com.rokefeli.colmenares.api.repository.CategoriaRepository;
import com.rokefeli.colmenares.api.repository.ProductoRepository;
import com.rokefeli.colmenares.api.service.interfaces.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaMapper mapper;

    @Override
    public List<CategoriaResponseDTO> findAll() {
        return categoriaRepository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<CategoriaResponseDTO> findAllActivos() {
        return categoriaRepository.findByEstado(EstadoCategoria.ACTIVO)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<CategoriaResponseDTO> findByEstado(EstadoCategoria estado) {
        return categoriaRepository.findByEstado(estado)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public CategoriaResponseDTO findByIdCliente(Long id) {
        Categoria existing = categoriaRepository.findByIdAndEstado(id, EstadoCategoria.ACTIVO)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
        return mapper.toResponseDTO(existing);
    }

    @Override
    public CategoriaResponseDTO findById(Long id) {
        Categoria existing = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
        return mapper.toResponseDTO(existing);
    }

    @Override
    public List<CategoriaResponseDTO> findByNameContainingIgnoreCaseAdmin(String name) {
        return categoriaRepository.findByNombreContainingIgnoreCase(name)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<CategoriaResponseDTO> findByNameContainingIgnoreCaseCliente(String name) {
        return categoriaRepository.findByNombreContainingIgnoreCaseAndEstado(name, EstadoCategoria.ACTIVO)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public CategoriaResponseDTO create(CategoriaCreateDTO createDTO) {
        if (categoriaRepository.existsByNombreIgnoreCase(createDTO.getNombre())) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre.");
        }
        Categoria categoria = mapper.toEntity(createDTO);
        categoria.setEstado(EstadoCategoria.ACTIVO);
        Categoria saved = categoriaRepository.save(categoria);
        return mapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public CategoriaResponseDTO update(Long id, CategoriaUpdateDTO updateDTO) {
        Categoria existing = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
        mapper.updateEntityFromDTO(updateDTO, existing);
        Categoria updated = categoriaRepository.save(existing);
        return mapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void desactivar(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));

        categoria.setEstado(EstadoCategoria.INACTIVO);
        categoriaRepository.save(categoria);

        List<Producto> productos = productoRepository.buscarProductos(null, id, EstadoProducto.ACTIVO);

        productos.forEach(p -> p.setEstado(EstadoProducto.DESCONTINUADO));

        productoRepository.saveAll(productos);
    }

    @Override
    @Transactional
    public void activar(Long id) {
        Categoria existing = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
        existing.setEstado(EstadoCategoria.ACTIVO);
        categoriaRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));

        if(productoRepository.existsByCategoria_Id(id)) {
            throw new IllegalArgumentException("No es posible borrar una categoria con productos registrados.");
        }

        categoriaRepository.deleteById(id);
    }
}
