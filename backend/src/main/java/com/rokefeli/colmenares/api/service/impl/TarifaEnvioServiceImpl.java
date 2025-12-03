package com.rokefeli.colmenares.api.service.impl;

import com.rokefeli.colmenares.api.dto.create.TarifaEnvioCreateDTO;
import com.rokefeli.colmenares.api.dto.response.TarifaEnvioResponseDTO;
import com.rokefeli.colmenares.api.dto.update.TarifaEnvioUpdateDTO;
import com.rokefeli.colmenares.api.entity.*;
import com.rokefeli.colmenares.api.entity.enums.*;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.TarifaEnvioMapper;
import com.rokefeli.colmenares.api.repository.AgenciaEnvioRepository;
import com.rokefeli.colmenares.api.repository.DistritoRepository;
import com.rokefeli.colmenares.api.repository.TarifaEnvioRepository;
import com.rokefeli.colmenares.api.service.interfaces.TarifaEnvioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TarifaEnvioServiceImpl implements TarifaEnvioService {

    @Autowired
    private TarifaEnvioRepository repository;

    @Autowired
    private AgenciaEnvioRepository agenciaRepository;

    @Autowired
    private DistritoRepository distritoRepository;

    @Autowired
    private TarifaEnvioMapper mapper;

    @Override
    public List<TarifaEnvioResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public TarifaEnvioResponseDTO findById(Long id) {
        TarifaEnvio existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TarifaEnvio", id));
        return mapper.toResponseDTO(existing);
    }

    @Override
    public List<TarifaEnvioResponseDTO> findByDistritoId(Long idDistrito) {
        return repository.findByDistrito_Id(idDistrito)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<TarifaEnvioResponseDTO> findByDistritoIdActivo(Long idDistrito) {
        return repository.findByDistrito_IdAndEstado(idDistrito, EstadoTarifa.ACTIVO)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<TarifaEnvioResponseDTO> findByAgenciaId(Long idAgencia) {
        return repository.findByAgenciaEnvio_Id(idAgencia)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public TarifaEnvioResponseDTO findByAgenciaIdAndDistritoId(Long idAgencia, Long idDistrito) {
        TarifaEnvio existing = repository.findByAgenciaEnvio_IdAndDistrito_Id(idAgencia, idDistrito)
                .orElseThrow(() -> new ResourceNotFoundException("TarifaEnvio"));
        return mapper.toResponseDTO(existing);
    }

    @Override
    public TarifaEnvioResponseDTO create(TarifaEnvioCreateDTO createDTO) {
        TarifaEnvio tarifaenvio = mapper.toEntity(createDTO);

        AgenciaEnvio agencia = agenciaRepository.findById(createDTO.getIdAgenciaEnvio())
                        .orElseThrow(() -> new ResourceNotFoundException("AgenciaEnvio", createDTO.getIdAgenciaEnvio()));

        Distrito distrito = distritoRepository.findById(createDTO.getIdDistrito())
                        .orElseThrow(() -> new ResourceNotFoundException("Distrito", createDTO.getIdDistrito()));

        tarifaenvio.setDistrito(distrito);
        tarifaenvio.setAgenciaEnvio(agencia);
        tarifaenvio.setEstado(EstadoTarifa.ACTIVO);

        TarifaEnvio saved = repository.save(tarifaenvio);
        return mapper.toResponseDTO(saved);
    }

    @Override
    public TarifaEnvioResponseDTO update(Long id, TarifaEnvioUpdateDTO updateDTO) {
        TarifaEnvio existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TarifaEnvio", id));
        mapper.updateEntityFromDTO(updateDTO, existing);
        TarifaEnvio updated = repository.save(existing);
        return mapper.toResponseDTO(updated);
    }

    @Override
    public void desactivar(Long id) {
        TarifaEnvio tarifa = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TarifaEnvio", id));
        tarifa.setEstado(EstadoTarifa.INACTIVO);
    }

    @Override
    public void activar(Long id) {
        // 1. Buscar tarifa
        TarifaEnvio existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TarifaEnvio", id));

        // 2. Obtener padres
        Distrito distritoPadre = existing.getDistrito();
        AgenciaEnvio agenciaPadre = existing.getAgenciaEnvio();

        // 3. Validaciones
        if (distritoPadre == null) {
            throw new IllegalStateException("La tarifa no tiene un distrito asignado.");
        }

        if (agenciaPadre == null) {
            throw new IllegalStateException("La tarifa no tiene una agencia asignada.");
        }

        if (distritoPadre.getEstado() == EstadoDistrito.INACTIVO || agenciaPadre.getEstado() == EstadoAgencia.INACTIVO) {
            throw new IllegalArgumentException("No es posible activar una tarifa de un distrito y/o agencia inactivos. Active el distrito y/o agencia primero.");
        }

        // 4. Activar y Guardar
        existing.setEstado(EstadoTarifa.ACTIVO);
        repository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("TarifaEnvio", id);
        }
        repository.deleteById(id);
    }
}
