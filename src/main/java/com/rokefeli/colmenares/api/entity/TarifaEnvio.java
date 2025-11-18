package com.rokefeli.colmenares.api.entity;

import com.rokefeli.colmenares.api.entity.enums.EstadoTarifa;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "tarifas_envio", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_agencia", "id_distrito"})
})
public class TarifaEnvio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_agencia", nullable = false)
    private AgenciaEnvio agenciaEnvio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_distrito", nullable = false)
    private Distrito distrito;

    @Column(name = "costo_envio", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoEnvio;

    @Column(name = "dias_estimados", nullable = false)
    private Long diasEstimados;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoTarifa estado;
}
