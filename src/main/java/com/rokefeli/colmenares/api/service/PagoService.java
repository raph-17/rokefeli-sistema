package com.rokefeli.colmenares.api.service;

import java.util.List;
import com.rokefeli.colmenares.api.entity.Pago;

public interface PagoService {
    List<Pago> findAll();
    Pago findById(Long id);
    Pago create(Pago pago);
    Pago update(Long id, Pago pago);
    void delete(Long id);
}
