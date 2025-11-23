package com.rokefeli.colmenares.api.service.impl;

import com.rokefeli.colmenares.api.dto.create.ProductoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.ProductoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.ProductoUpdateDTO;
import com.rokefeli.colmenares.api.dto.update.StockAdjustmentDTO;
import com.rokefeli.colmenares.api.entity.Categoria;
import com.rokefeli.colmenares.api.entity.Producto;
import com.rokefeli.colmenares.api.entity.enums.EstadoCategoria;
import com.rokefeli.colmenares.api.entity.enums.EstadoProducto;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.ProductoMapper;
import com.rokefeli.colmenares.api.repository.CategoriaRepository;
import com.rokefeli.colmenares.api.repository.ProductoRepository;
import com.rokefeli.colmenares.api.service.interfaces.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoMapper mapper;

    @Override
    public List<ProductoResponseDTO> findAll() {
        return productoRepository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<ProductoResponseDTO> findAllActivos() {
        return productoRepository.findByEstado(EstadoProducto.ACTIVO)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public ProductoResponseDTO findById(Long id) {
        Producto existing = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
        return mapper.toResponseDTO(existing);
    }

    @Override
    public ProductoResponseDTO findByIdCliente(Long id) {
        Producto existing = productoRepository.findByIdAndEstado(id, EstadoProducto.ACTIVO)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
        return mapper.toResponseDTO(existing);
    }

    @Override
    public List<ProductoResponseDTO> buscarCliente(String nombre, Long idCategoria) {
        return productoRepository.buscarProductos(nombre, idCategoria, EstadoProducto.ACTIVO)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<ProductoResponseDTO> buscarAdmin(String nombre, Long idCategoria, EstadoProducto estado) {
        return productoRepository.buscarProductos(nombre, idCategoria, estado)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public ProductoResponseDTO create(ProductoCreateDTO createDTO) {
        Categoria categoria = categoriaRepository.findByIdAndEstado(createDTO.getIdCategoria(),
                        EstadoCategoria.ACTIVO)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria",
                        createDTO.getIdCategoria())); // Busca la categoria asociada

        if(productoRepository.existsByNombre(createDTO.getNombre().trim())) {
            throw new IllegalArgumentException("No es posible crear dos productos con el mismo nombre.");
        }

        Producto producto = mapper.toEntity(createDTO); // Crea el producto a partir del DTO
        producto.setCategoria(categoria); // Asocia la categoria al producto
        producto.setEstado(EstadoProducto.ACTIVO); // Inicializa el producto como ACTIVO
        Producto saved = productoRepository.save(producto); // Guarda el producto en la base de datos
        return mapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public ProductoResponseDTO update(Long id, ProductoUpdateDTO updateDTO) {
        Producto existing = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id)); // Confirma que el producto existe
        Categoria categoria = categoriaRepository.findByIdAndEstado(updateDTO.getIdCategoria(), EstadoCategoria.ACTIVO)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", updateDTO.getIdCategoria())); // Busca la categoria a asociar
        mapper.updateEntityFromDTO(updateDTO, existing);
        existing.setCategoria(categoria); // Actualiza la categoria del producto
        Producto updated = productoRepository.save(existing);
        return mapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void descontinuar(Long id) {
        Producto existing = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
        existing.setEstado(EstadoProducto.DESCONTINUADO);
        productoRepository.save(existing);
    }

    @Override
    @Transactional
    public void reintegrar(Long id) {
        Producto existing = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
        existing.setEstado(EstadoProducto.ACTIVO);
        productoRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto", id);
        }
        productoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void ajustarStock(StockAdjustmentDTO dto) {
        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto", dto.getProductoId()));
        int nuevoStock = producto.getStockActual() + dto.getCantidadCambio();
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        producto.setStockActual(nuevoStock);
        productoRepository.save(producto);
    }
}
