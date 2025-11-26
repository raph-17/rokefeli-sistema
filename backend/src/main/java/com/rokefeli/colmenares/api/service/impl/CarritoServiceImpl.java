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
import java.util.List;

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

    @Autowired
    private DetalleCarritoMapper detalleMapper;

    @Override
    public CarritoResponseDTO verCarrito(Long idUsuario) {
        Carrito carrito = obtenerOCrearCarritoActivo(idUsuario);
        return carritoMapper.toResponseDTO(carrito);
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
            detalle.setProducto(producto);
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setCantidad(dto.getCantidad());
        } else {
            int nuevaCantidad = detalle.getCantidad() + dto.getCantidad();
            if (producto.getStockActual() < nuevaCantidad)
                throw new IllegalArgumentException("Stock insuficiente para aumentar la cantidad.");
            detalle.setCantidad(nuevaCantidad);
        }

        detalle.calcularSubtotal();
        detalleRepository.save(detalle);

        carrito.calcularTotal();
        carritoRepository.save(carrito);

        return carritoMapper.toResponseDTO(carrito);
    }

    @Override
    public CarritoResponseDTO actualizarCantidad(Long idUsuario, Long idProducto, Integer nuevaCantidad) {

        Carrito carrito = obtenerOCrearCarritoActivo(idUsuario);

        if (nuevaCantidad <= 0)
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");

        DetalleCarrito detalle = detalleRepository
                .findByCarrito_IdAndProducto_Id(carrito.getId(), idProducto)
                .orElseThrow(() -> new ResourceNotFoundException("DetalleCarrito"));

        Producto producto = detalle.getProducto();

        if (producto.getStockActual() < nuevaCantidad)
            throw new IllegalArgumentException("Stock insuficiente.");

        detalle.setCantidad(nuevaCantidad);
        detalle.calcularSubtotal();
        detalleRepository.save(detalle);

        carrito.calcularTotal();
        carritoRepository.save(carrito);

        return carritoMapper.toResponseDTO(carrito);
    }

    @Override
    public CarritoResponseDTO eliminarProducto(Long idUsuario, Long idProducto) {

        Carrito carrito = obtenerOCrearCarritoActivo(idUsuario);

        DetalleCarrito detalle = detalleRepository
                .findByCarrito_IdAndProducto_Id(carrito.getId(), idProducto)
                .orElseThrow(() -> new ResourceNotFoundException("DetalleCarrito"));

        detalleRepository.delete(detalle);

        carrito.getDetalles().remove(detalle);
        carrito.calcularTotal();
        carritoRepository.save(carrito);

        return carritoMapper.toResponseDTO(carrito);
    }

    @Override
    public void vaciarCarrito(Long idUsuario) {
        Carrito carrito = obtenerOCrearCarritoActivo(idUsuario);
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
}
