package com.rokefeli.colmenares.api.repository;

import com.rokefeli.colmenares.api.entity.TarifaEnvio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TarifaEnvioRepository extends JpaRepository<TarifaEnvio, Long> {
    List<TarifaEnvio> findByAgenciaEnvio_Id(Long agenciaEnvioId);
}
