package com.rokefeli.colmenares.api.service;

import java.util.List;
import com.rokefeli.colmenares.api.entity.TarifaEnvio;

public interface TarifaEnvioService {
    List<TarifaEnvio> findAll();
    TarifaEnvio findById(Long id);
    TarifaEnvio create(TarifaEnvio tarifaenvio);
    TarifaEnvio update(Long id, TarifaEnvio tarifaenvio);
    void delete(Long id);
}
