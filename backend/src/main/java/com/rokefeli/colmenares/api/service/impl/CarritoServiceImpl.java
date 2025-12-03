package com.rokefeli.colmenares.api.service.impl;

import com.rokefeli.colmenares.api.dto.create.DetalleCarritoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.CarritoResponseDTO;
import com.rokefeli.colmenares.api.entity.Carrito;
import com.rokefeli.colmenares.api.entity.DetalleCarrito;
import com.rokefeli.colmenares.api.entity.Producto;
import com.rokefeli.colmenares.api.entity.Usuario;
import com.rokefeli.colmenares.api.entity.enums.EstadoCarrito;
import com.rokefeli.colmenares.api.entity.enums.EstadoProducto;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.CarritoMapper;
import com.rokefeli.colmenares.api.mapper.DetalleCarritoMapper;
import com.rokefeli.colmenares.api.repository.CarritoRepository;
import com.rokefeli.colmenares.api.repository.DetalleCarritoRepository;
import com.rokefeli.colmenares.api.repository.ProductoRepository;
import com.rokefeli.colmenares.api.repository.UsuarioRepository;
import com.rokefeli.colmenares.api.service.interfaces.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class CarritoServiceImpl implements CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;
    @Autowired
    private DetalleCarritoRepository detalleRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CarritoMapper carritoMapper;

    // --- MÉTODOS PÚBLICOS ---

    @Override
    @Transactional(readOnly = true)
    public CarritoResponseDTO verCarrito(Long idUsuario) {
        Carrito carrito = carritoRepository.findByUsuario_Id(idUsuario)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        // Usamos el método auxiliar para garantizar que lleve los datos
        return convertirConDatosCompletos(carrito);
    }

    @Override
    public CarritoResponseDTO agregarProducto(Long idUsuario, DetalleCarritoCreateDTO dto) {
        Carrito carrito = obtenerOCrearCarritoActivo(idUsuario);

        Producto producto = productoRepository.findById(dto.getIdProducto())
                .orElseThrow(() -> new ResourceNotFoundException("Producto", dto.getIdProducto()));

        if (producto.getEstado().equals(EstadoProducto.DESCONTINUADO))
            throw new IllegalArgumentException("Este producto ya no está disponible.");

        if (producto.getStockActual() < dto.getCantidad())
            throw new IllegalArgumentException("No hay suficiente stock del producto.");

        DetalleCarrito detalle = detalleRepository
                .findByCarrito_IdAndProducto_Id(carrito.getId(), producto.getId())
                .orElse(null);

        if (detalle == null) {
            detalle = new DetalleCarrito();
            detalle.setCarrito(carrito);
            detalle.setProducto(producto); // AQUÍ SE GUARDA BIEN EN BD
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setCantidad(dto.getCantidad());
            carrito.getDetalles().add(detalle); // Importante agregar a la lista en memoria
        } else {
            int nuevaCantidad = detalle.getCantidad() + dto.getCantidad();
            if (producto.getStockActual() < nuevaCantidad)
                throw new IllegalArgumentException("Stock insuficiente.");
            detalle.setCantidad(nuevaCantidad);
        }

        detalle.calcularSubtotal();
        // Guardamos el carrito (cascade guarda detalles)
        carrito.calcularTotal();
        carrito = carritoRepository.save(carrito);

        // Retornamos con el parche manual
        return convertirConDatosCompletos(carrito);
    }

    @Override
    public CarritoResponseDTO actualizarCantidad(Long idUsuario, Long idProducto, Integer nuevaCantidad) {
        Carrito carrito = obtenerOCrearCarritoActivo(idUsuario);

        if (nuevaCantidad <= 0) throw new IllegalArgumentException("Cantidad debe ser mayor a 0");

        DetalleCarrito detalle = detalleRepository
                .findByCarrito_IdAndProducto_Id(carrito.getId(), idProducto)
                .orElseThrow(() -> new ResourceNotFoundException("DetalleCarrito"));

        if (detalle.getProducto().getStockActual() < nuevaCantidad)
            throw new IllegalArgumentException("Stock insuficiente.");

        detalle.setCantidad(nuevaCantidad);
        detalle.calcularSubtotal();
        detalleRepository.save(detalle);

        carrito.calcularTotal();
        carritoRepository.save(carrito);

        return convertirConDatosCompletos(carrito);
    }

    @Override
    public CarritoResponseDTO eliminarProducto(Long idUsuario, Long idProducto) {
        Carrito carrito = obtenerOCrearCarritoActivo(idUsuario);

        DetalleCarrito detalle = detalleRepository
                .findByCarrito_IdAndProducto_Id(carrito.getId(), idProducto)
                .orElseThrow(() -> new ResourceNotFoundException("DetalleCarrito"));

        // Removemos de la lista en memoria y de la BD
        carrito.getDetalles().remove(detalle);
        detalleRepository.delete(detalle);

        carrito.calcularTotal();
        carritoRepository.save(carrito);

        return convertirConDatosCompletos(carrito);
    }

    @Override
    public void vaciarCarrito(Long idUsuario) {
        Carrito carrito = obtenerOCrearCarritoActivo(idUsuario);
        // La forma más limpia de vaciar en JPA manteniendo la relación
        // es borrar los detalles primero o usar orphanRemoval=true en la entidad
        detalleRepository.deleteAll(carrito.getDetalles());
        carrito.getDetalles().clear();

        carrito.setMontoTotal(BigDecimal.ZERO);
        carritoRepository.save(carrito);
    }

    @Override
    public void marcarComoComprado(Long idUsuario) {
        Carrito carrito = obtenerOCrearCarritoActivo(idUsuario);
        carrito.setEstado(EstadoCarrito.COMPRADO);
        carritoRepository.save(carrito);
    }

    // --- MÉTODOS PRIVADOS ---

    private Carrito obtenerOCrearCarritoActivo(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", idUsuario));

        return carritoRepository
                .findByUsuario_IdAndEstado(idUsuario, EstadoCarrito.ACTIVO)
                .orElseGet(() -> {
                    Carrito nuevo = new Carrito();
                    nuevo.setUsuario(usuario);
                    nuevo.setEstado(EstadoCarrito.ACTIVO);
                    nuevo.setMontoTotal(BigDecimal.ZERO);
                    return carritoRepository.save(nuevo);
                });
    }

    /**
     * Este método centraliza tu "Parche Manual".
     * Se asegura de que el DTO siempre salga con nombre, imagen y ID de producto,
     * ignorando si el Mapper falló o si Hibernate estaba Lazy.
     */
    private CarritoResponseDTO convertirConDatosCompletos(Carrito carrito) {
        // 1. Mapeo automático básico
        CarritoResponseDTO dto = carritoMapper.toResponseDTO(carrito);

        // 2. Parche manual (Tu lógica, pero ahora reutilizable)
        if (carrito.getDetalles() != null && dto.getDetalles() != null) {

            // Usamos dos índices para recorrer ambas listas en paralelo
            // Asumiendo que el mapper mantiene el orden (generalmente sí)
            for (int i = 0; i < carrito.getDetalles().size(); i++) {
                if (i >= dto.getDetalles().size()) break; // Seguridad

                DetalleCarrito entidad = carrito.getDetalles().get(i);
                var detalleDTO = dto.getDetalles().get(i);

                if (entidad.getProducto() != null) {
                    detalleDTO.setIdProducto(entidad.getProducto().getId());
                    detalleDTO.setNombreProducto(entidad.getProducto().getNombre());
                    detalleDTO.setImagenUrl(entidad.getProducto().getImagenUrl());

                    // Aseguramos precio si falta
                    if(detalleDTO.getPrecioUnitario() == null) {
                        detalleDTO.setPrecioUnitario(entidad.getProducto().getPrecio());
                    }
                }
            }
        }
        return dto;
    }
}