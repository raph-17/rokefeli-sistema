package com.rokefeli.colmenares.api.controller;

import com.rokefeli.colmenares.api.dto.create.PagoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.PagoResponseDTO;
import com.rokefeli.colmenares.api.service.interfaces.PagoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    // Cliente realiza pago de su propia compra
    @PostMapping
    @PreAuthorize("@securityService.isVentaOwner(authentication, #dto.idVenta)")
    public ResponseEntity<?> realizarPago(@Valid @RequestBody PagoCreateDTO dto) {
        PagoResponseDTO respuesta = pagoService.procesarPago(dto);
        return ResponseEntity.ok().body(respuesta);
    }
}