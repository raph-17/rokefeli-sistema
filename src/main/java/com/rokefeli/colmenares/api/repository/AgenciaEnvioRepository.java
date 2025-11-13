package com.rokefeli.colmenares.api.repository;

import com.rokefeli.colmenares.api.entity.AgenciaEnvio;
import com.rokefeli.colmenares.api.entity.enums.EstadoAgencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgenciaEnvioRepository extends JpaRepository<AgenciaEnvio, Long> {
    List<AgenciaEnvio> findByEstado(EstadoAgencia estado);
    boolean existsByNombre(String nombre);
}
