package com.rokefeli.colmenares.api.entity;

import com.rokefeli.colmenares.api.entity.enums.EstadoPedido;
import com.rokefeli.colmenares.api.entity.enums.ModalidadEntrega;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"venta", "distrito", "agenciaEnvio"})
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_venta", nullable = false)
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "id_distrito", nullable = false)
    private Distrito distrito;

    @ManyToOne
    @JoinColumn(name = "id_agencia", nullable = false)
    private AgenciaEnvio agenciaEnvio;

    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;

    @Column(name = "fecha_estimada", nullable = false)
    private LocalDateTime fechaEstimada;

    @Column(name = "direccion_envio", columnDefinition = "TEXT")
    private String direccionEnvio;

    @CreationTimestamp
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoPedido estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "modalidad_entrega", nullable = false)
    private ModalidadEntrega modalidadEntrega;

    @Column(name = "referencia_cliente", columnDefinition = "TEXT")
    private String referenciaCliente;
}
