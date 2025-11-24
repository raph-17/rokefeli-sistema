package com.rokefeli.colmenares.api.service.impl;

import com.rokefeli.colmenares.api.dto.create.DetalleVentaCreateDTO;
import com.rokefeli.colmenares.api.dto.create.VentaInternoCreateDTO;
import com.rokefeli.colmenares.api.dto.create.VentaOnlineCreateDTO;
import com.rokefeli.colmenares.api.dto.response.VentaResponseDTO;
import com.rokefeli.colmenares.api.entity.DetalleVenta;
import com.rokefeli.colmenares.api.entity.Producto;
import com.rokefeli.colmenares.api.entity.Usuario;
import com.rokefeli.colmenares.api.entity.Venta;
import com.rokefeli.colmenares.api.entity.enums.CanalVenta;
import com.rokefeli.colmenares.api.entity.enums.EstadoVenta;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.DetalleVentaMapper;
import com.rokefeli.colmenares.api.mapper.VentaMapper;
import com.rokefeli.colmenares.api.repository.ProductoRepository;
import com.rokefeli.colmenares.api.repository.UsuarioRepository;
import com.rokefeli.colmenares.api.repository.VentaRepository;
import com.rokefeli.colmenares.api.service.interfaces.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private VentaMapper ventaMapper;

    @Autowired
    private DetalleVentaMapper detalleVentaMapper;

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

        return ventaMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public VentaResponseDTO registrarInterno(VentaInternoCreateDTO dto) {
        Usuario distribuidor = usuarioRepository.findById(dto.getIdClienteDistribuidor())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", dto.getIdClienteDistribuidor()));

        Usuario empleado = usuarioRepository.findById(dto.getIdEmpleado())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario empleado", dto.getIdEmpleado()));

        Venta venta = new Venta();
        venta.setUsuario(distribuidor);
        venta.setEmpleadoRegistra(empleado);
        venta.setEstado(EstadoVenta.PROCESADA); // Se completa sin pago
        venta.setCanal(CanalVenta.INTERNO);

        procesarDetallesYStock(venta, dto.getDetalles());

        Venta saved = ventaRepository.save(venta);

        return ventaMapper.toResponseDTO(saved);
    }

    @Override
    public VentaResponseDTO findById(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", id));

        return buildResponse(venta, venta.getDetalles());
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
            det.setSubtotal(producto.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad())));

            venta.getDetalles().add(det);

            total = total.add(det.getSubtotal());
        }

        venta.setMontoTotal(total);
    }
}

