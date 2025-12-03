package com.rokefeli.colmenares.api.service.impl;

import com.rokefeli.colmenares.api.dto.create.DetalleVentaCreateDTO;
import com.rokefeli.colmenares.api.dto.create.VentaInternoCreateDTO;
import com.rokefeli.colmenares.api.dto.create.VentaOnlineCreateDTO;
import com.rokefeli.colmenares.api.dto.response.VentaResponseDTO;
import com.rokefeli.colmenares.api.entity.*;
import com.rokefeli.colmenares.api.entity.enums.CanalVenta;
import com.rokefeli.colmenares.api.entity.enums.EstadoCarrito;
import com.rokefeli.colmenares.api.entity.enums.EstadoVenta;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.DetalleVentaMapper;
import com.rokefeli.colmenares.api.mapper.VentaMapper;
import com.rokefeli.colmenares.api.repository.CarritoRepository;
import com.rokefeli.colmenares.api.repository.ProductoRepository;
import com.rokefeli.colmenares.api.repository.UsuarioRepository;
import com.rokefeli.colmenares.api.repository.VentaRepository;
import com.rokefeli.colmenares.api.service.interfaces.CarritoService;
import com.rokefeli.colmenares.api.service.interfaces.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class VentaServiceImpl implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private VentaMapper ventaMapper;

    @Autowired
    private DetalleVentaMapper detalleVentaMapper;

    @Autowired
    private CarritoService carritoService;

    @Override
    @Transactional
    public VentaResponseDTO registrarOnline(VentaOnlineCreateDTO dto) {

        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", dto.getIdUsuario()));

        Venta venta = ventaMapper.toEntity(dto);
        venta.setUsuario(usuario);
        venta.setCanal(CanalVenta.ONLINE);
        venta.setEstado(EstadoVenta.PENDIENTE);

        procesarDetallesYStock(venta, dto.getDetalles());

        Venta saved = ventaRepository.save(venta);

        carritoService.marcarComoComprado(dto.getIdUsuario());

        return buildResponse(saved, saved.getDetalles());
    }

    @Override
    public VentaResponseDTO crearVentaDesdeCarrito(Long idUsuario) {

        // 1. Buscar el carrito activo del usuario
        Carrito carrito = carritoRepository.findByUsuario_IdAndEstado(idUsuario, EstadoCarrito.ACTIVO)
                .orElseThrow(() -> new RuntimeException("No hay un carrito activo para este usuario."));

        // 2. Validar que no esté vacío
        if (carrito.getDetalles() == null || carrito.getDetalles().isEmpty()) {
            throw new RuntimeException("El carrito está vacío, no se puede iniciar el checkout.");
        }

        // 3. Crear la Cabecera de la Venta
        Venta venta = new Venta();
        venta.setUsuario(carrito.getUsuario());
        venta.setEstado(EstadoVenta.PENDIENTE); // Importante: Aún no pagada
        venta.setCanal(CanalVenta.ONLINE);
        venta.setMontoTotal(carrito.getMontoTotal()); // Copiamos el total

        // 4. Copiar Detalles (Carrito -> Venta)
        List<DetalleVenta> detallesVenta = new ArrayList<>();

        for (DetalleCarrito detCarrito : carrito.getDetalles()) {
            DetalleVenta detVenta = new DetalleVenta();
            detVenta.setVenta(venta);
            detVenta.setProducto(detCarrito.getProducto());
            detVenta.setCantidad(detCarrito.getCantidad());
            detVenta.setPrecioUnitario(detCarrito.getPrecioUnitario());
            detVenta.setSubtotal(detCarrito.getSubtotal());

            detallesVenta.add(detVenta);
        }

        venta.setDetalles(detallesVenta);

        // 5. Guardar la Venta (Cascada guardará los detalles)
        Venta ventaGuardada = ventaRepository.save(venta);

        // 6. Retornar DTO
        return ventaMapper.toResponseDTO(ventaGuardada);
    }

    @Override
    @Transactional
    public VentaResponseDTO registrarInterno(VentaInternoCreateDTO dto) {
        Usuario distribuidor = null;

        if (dto.getIdDistribuidor() != null) {
            distribuidor = usuarioRepository.findById(dto.getIdDistribuidor())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario", dto.getIdDistribuidor()));
        }

        Usuario empleado = usuarioRepository.findById(dto.getIdEmpleado())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario empleado", dto.getIdEmpleado()));

        Venta venta = new Venta();
        venta.setUsuario(empleado);
        venta.setDistribuidor(distribuidor);
        venta.setEstado(EstadoVenta.PROCESADA); // Se completa sin pago
        venta.setCanal(CanalVenta.INTERNO);

        procesarDetallesYStock(venta, dto.getDetalles());

        Venta saved = ventaRepository.save(venta);

        return buildResponse(saved, saved.getDetalles());
    }

    @Override
    public VentaResponseDTO findById(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", id));

        return buildResponse(venta, venta.getDetalles());
    }

    @Override
    public List<VentaResponseDTO> findByEstadoCliente(Long id, EstadoVenta estado) {
        return ventaRepository.findByUsuario_IdAndEstado(id, estado)
                .stream()
                .map(v -> buildResponse(v, v.getDetalles()))
                .toList();
    }

    @Override
    public List<VentaResponseDTO> findAll() {
        return ventaRepository.findAll()
                .stream()
                .map(v -> buildResponse(v, v.getDetalles()))
                .toList();
    }

    @Override
    public List<VentaResponseDTO> findByUsuario(Long idUsuario) {
        return ventaRepository.findByUsuario_Id(idUsuario)
                .stream()
                .map(v -> buildResponse(v, v.getDetalles()))
                .toList();
    }

    @Override
    public List<VentaResponseDTO> buscarAdmin(EstadoVenta estado, CanalVenta canal, String dni) {
        return ventaRepository.buscarVentasAdmin(estado, canal, dni)
                .stream()
                .map(v -> buildResponse(v, v.getDetalles()))
                .toList();
    }

    private VentaResponseDTO buildResponse(Venta venta, List<DetalleVenta> detalles) {

        VentaResponseDTO response = ventaMapper.toResponseDTO(venta);

        response.setDetalles(
                detalles.stream()
                        .map(detalleVentaMapper::toResponseDTO)
                        .toList()
        );

        return response;
    }

    private void procesarDetallesYStock(Venta venta, List<DetalleVentaCreateDTO> detalles) {

        BigDecimal total = BigDecimal.ZERO;

        List<DetalleVenta> details = new ArrayList<>();

        for (DetalleVentaCreateDTO item : detalles) {

            Producto producto = productoRepository.findById(item.getIdProducto())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto", item.getIdProducto()));

            if (producto.getStockActual() < item.getCantidad()) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para el producto: " + producto.getNombre()
                );
            }

            // descontar stock
            producto.setStockActual(producto.getStockActual() - item.getCantidad());
            productoRepository.save(producto);

            // crear detalle
            DetalleVenta det = new DetalleVenta();
            det.setVenta(venta);
            det.setProducto(producto);
            det.setCantidad(item.getCantidad());
            det.setPrecioUnitario(producto.getPrecio());
            det.setSubtotal(det.calcularSubtotal());

            details.add(det);

            total = total.add(det.getSubtotal());
        }

        venta.setDetalles(details);

        venta.setMontoTotal(total);
    }
}

