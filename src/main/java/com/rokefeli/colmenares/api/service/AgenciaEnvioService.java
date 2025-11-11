package com.rokefeli.colmenares.api.service;

import java.util.List;
import com.rokefeli.colmenares.api.entity.AgenciaEnvio;

public interface AgenciaEnvioService {
    List<AgenciaEnvio> findAll();
    AgenciaEnvio findById(Long id);
    AgenciaEnvio create(AgenciaEnvio agenciaenvio);
    AgenciaEnvio update(Long id, AgenciaEnvio agenciaenvio);
    void delete(Long id);
}
