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
        Carrito carrito = carritoRepository.findByUsuario_IdAndEstado(idUsuario, EstadoCarrito.ACTIVO)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado o inactivo"));

        return convertirConDatosCompletos(carrito);
    }

    @Override
    public CarritoResponseDTO agregarProducto(Long idUsuario, DetalleCarritoCreateDTO dto) {
        Carrito carrito = obtenerOCrearCarritoActivo(idUsuario);

        Producto producto = productoRepository.findById(dto.getIdProducto())
                .orElseThrow(() -> new ResourceNotFoundException("Producto", dto.getIdProducto()));

        if (producto.getEstado().equals(EstadoProducto.DESCONTINUADO))
            throw new IllegalArgumentException("Este producto ya no está disponible.");

        // 1. VALIDAR STOCK
        if (producto.getStockActual() < dto.getCantidad())
            throw new IllegalArgumentException("No hay suficiente stock. Quedan: " + producto.getStockActual());

        // 2. RESTAR STOCK (Reserva)
        producto.setStockActual(producto.getStockActual() - dto.getCantidad());
        productoRepository.save(producto);

        DetalleCarrito detalle = detalleRepository
                .findByCarrito_IdAndProducto_Id(carrito.getId(), producto.getId())
                .orElse(null);

        if (detalle == null) {
            detalle = new DetalleCarrito();
            detalle.setCarrito(carrito);
            detalle.setProducto(producto);
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setCantidad(dto.getCantidad());
            carrito.getDetalles().add(detalle);
        } else {
            int nuevaCantidad = detalle.getCantidad() + dto.getCantidad();
            detalle.setCantidad(nuevaCantidad);
        }

        detalle.calcularSubtotal();
        detalleRepository.save(detalle); // Asegurar ID del detalle

        carrito.calcularTotal();
        carrito = carritoRepository.save(carrito);

        return convertirConDatosCompletos(carrito);
    }

    @Override
    public CarritoResponseDTO actualizarCantidad(Long idUsuario, Long idProducto, Integer nuevaCantidad) {
        Carrito carrito = obtenerOCrearCarritoActivo(idUsuario);

        if (nuevaCantidad <= 0) throw new IllegalArgumentException("Cantidad debe ser mayor a 0");

        DetalleCarrito detalle = detalleRepository
                .findByCarrito_IdAndProducto_Id(carrito.getId(), idProducto)
                .orElseThrow(() -> new ResourceNotFoundException("DetalleCarrito"));

        Producto producto = detalle.getProducto();

        // Lógica del stock al actualizar
        int cantidadActual = detalle.getCantidad();
        int diferencia = nuevaCantidad - cantidadActual;

        if (diferencia > 0) {
            // Quiere comprar MÁS -> Validar y Restar Stock
            if (producto.getStockActual() < diferencia) {
                throw new IllegalArgumentException("Stock insuficiente para agregar " + diferencia + " unidades más.");
            }
            producto.setStockActual(producto.getStockActual() - diferencia);
        } else if (diferencia < 0) {
            // Quiere comprar MENOS -> Devolver Stock
            producto.setStockActual(producto.getStockActual() + Math.abs(diferencia));
        }

        productoRepository.save(producto); // Guardamos nuevo inventario

        detalle.setCantidad(nuevaCantidad);
        detalle.calcularSubtotal();
        detalleRepository.save(detalle);

        carrito.calcularTotal();
        carrito = carritoRepository.save(carrito);

        return convertirConDatosCompletos(carrito);
    }

    @Override
    public CarritoResponseDTO eliminarProducto(Long idUsuario, Long idProducto) {
        Carrito carrito = obtenerOCrearCarritoActivo(idUsuario);

        DetalleCarrito detalle = detalleRepository
                .findByCarrito_IdAndProducto_Id(carrito.getId(), idProducto)
                .orElseThrow(() -> new ResourceNotFoundException("DetalleCarrito"));

        // Devolver stock al eliminar
        Producto p = detalle.getProducto();
        p.setStockActual(p.getStockActual() + detalle.getCantidad());
        productoRepository.save(p);

        // Remover de la lista y borrar
        carrito.getDetalles().remove(detalle);
        detalleRepository.delete(detalle);

        carrito.calcularTotal();
        carrito = carritoRepository.save(carrito);

        return convertirConDatosCompletos(carrito);
    }

    @Override
    public void vaciarCarrito(Long idUsuario) {
        Carrito carrito = obtenerOCrearCarritoActivo(idUsuario);

        // Devolver todo el stock
        for (DetalleCarrito det : carrito.getDetalles()) {
            Producto p = det.getProducto();
            p.setStockActual(p.getStockActual() + det.getCantidad());
            productoRepository.save(p);
        }

        detalleRepository.deleteAll(carrito.getDetalles());
        carrito.getDetalles().clear();

        carrito.setMontoTotal(BigDecimal.ZERO);
        carritoRepository.save(carrito);
    }

    @Override
    public void marcarComoComprado(Long idUsuario) {
        // Este método se llama cuando el pago fue EXITOSO.
        // NO se devuelve stock, solo se cierra el carrito.
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

    private CarritoResponseDTO convertirConDatosCompletos(Carrito carrito) {
        // 1. Mapeo automático básico
        CarritoResponseDTO dto = carritoMapper.toResponseDTO(carrito);

        // 2. Parche manual para asegurar datos (ID, Nombre, Imagen, Precio)
        if (carrito.getDetalles() != null && dto.getDetalles() != null) {
            for (int i = 0; i < carrito.getDetalles().size(); i++) {
                if (i >= dto.getDetalles().size()) break;

                DetalleCarrito entidad = carrito.getDetalles().get(i);
                var detalleDTO = dto.getDetalles().get(i);

                if (entidad.getProducto() != null) {
                    detalleDTO.setIdProducto(entidad.getProducto().getId());
                    detalleDTO.setNombreProducto(entidad.getProducto().getNombre());
                    detalleDTO.setImagenUrl(entidad.getProducto().getImagenUrl());

                    if(detalleDTO.getPrecioUnitario() == null) {
                        detalleDTO.setPrecioUnitario(entidad.getProducto().getPrecio());
                    }
                }
            }
        }
        return dto;
    }
}