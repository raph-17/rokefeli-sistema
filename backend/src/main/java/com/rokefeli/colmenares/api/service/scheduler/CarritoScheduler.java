package com.rokefeli.colmenares.api.service.scheduler;

import com.rokefeli.colmenares.api.entity.Carrito;
import com.rokefeli.colmenares.api.entity.DetalleCarrito;
import com.rokefeli.colmenares.api.entity.Producto;
import com.rokefeli.colmenares.api.entity.enums.EstadoCarrito;
import com.rokefeli.colmenares.api.repository.CarritoRepository;
import com.rokefeli.colmenares.api.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class CarritoScheduler {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // Se ejecuta cada minuto (60000 ms) para revisar
    @Scheduled(fixedRate = 60000)
    @Transactional // Importante para manejar la devolución de stock
    public void liberarCarritosAbandonados() {

        // 1. Definir el límite (Hace 2 horas)
        LocalDateTime haceDosHoras = LocalDateTime.now().minusHours(2);

        // 2. Buscar carritos "viejos"
        List<Carrito> carritosExpirados = carritoRepository
                .findByEstadoAndFechaActualizacionBefore(EstadoCarrito.ACTIVO, haceDosHoras);

        if (carritosExpirados.isEmpty()) {
            return; // Nada que hacer
        }

        System.out.println("🧹 SCHEDULER: Encontrados " + carritosExpirados.size() + " carritos abandonados. Liberando stock...");

        // 3. Procesar cada carrito
        for (Carrito carrito : carritosExpirados) {

            // Devolver Stock de cada producto
            for (DetalleCarrito detalle : carrito.getDetalles()) {
                Producto p = detalle.getProducto();
                p.setStockActual(p.getStockActual() + detalle.getCantidad());
                productoRepository.save(p);
            }

            // Cambiar estado a ABANDONADO (o podrías borrarlos con .delete)
            // Es mejor marcarlos como abandonados para métricas futuras
            carrito.setEstado(EstadoCarrito.EXPIRADO);
            carritoRepository.save(carrito);

            System.out.println("   -> Carrito ID " + carrito.getId() + " expirado y stock devuelto.");
        }
    }
}