package com.rokefeli.colmenares.api.service.impl;

import com.rokefeli.colmenares.api.dto.create.DepartamentoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.DepartamentoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.DepartamentoUpdateDTO;
import com.rokefeli.colmenares.api.entity.Departamento;
import com.rokefeli.colmenares.api.entity.Distrito;
import com.rokefeli.colmenares.api.entity.Provincia;
import com.rokefeli.colmenares.api.entity.TarifaEnvio;
import com.rokefeli.colmenares.api.entity.enums.EstadoDepartamento;
import com.rokefeli.colmenares.api.entity.enums.EstadoDistrito;
import com.rokefeli.colmenares.api.entity.enums.EstadoProvincia;
import com.rokefeli.colmenares.api.entity.enums.EstadoTarifa;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.DepartamentoMapper;
import com.rokefeli.colmenares.api.repository.DepartamentoRepository;
import com.rokefeli.colmenares.api.repository.DistritoRepository;
import com.rokefeli.colmenares.api.repository.ProvinciaRepository;
import com.rokefeli.colmenares.api.repository.TarifaEnvioRepository;
import com.rokefeli.colmenares.api.service.interfaces.DepartamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DepartamentoServiceImpl implements DepartamentoService {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private ProvinciaRepository provinciaRepository;

    @Autowired
    private DistritoRepository distritoRepository;

    @Autowired
    private DepartamentoMapper mapper;

    @Autowired
    private TarifaEnvioRepository tarifaEnvioRepository;

    @Override
    public List<DepartamentoResponseDTO> findAll() {
        return departamentoRepository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public DepartamentoResponseDTO findById(Long id) {
        Departamento existing = departamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento", id));
        return mapper.toResponseDTO(existing);
    }

    @Override
    public List<DepartamentoResponseDTO> findByEstado(EstadoDepartamento estado) {
        return departamentoRepository.findByEstado(estado)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public DepartamentoResponseDTO create(DepartamentoCreateDTO createDTO) {
        if(departamentoRepository.existsByNombreIgnoreCase(createDTO.getNombre())) {
            throw new IllegalStateException("El departamento con nombre " + createDTO.getNombre() + " ya existe.");
        }
        Departamento departamento = mapper.toEntity(createDTO);
        departamento.setEstado(EstadoDepartamento.ACTIVO);
        Departamento saved = departamentoRepository.save(departamento);
        return mapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public DepartamentoResponseDTO update(Long id, DepartamentoUpdateDTO updateDTO) {
        Departamento existing = departamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento", id));
        mapper.updateEntityFromDTO(updateDTO, existing);
        Departamento updated = departamentoRepository.save(existing);
        return mapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void desactivar(Long id) {
        // 1. Validar y desactivar el Departamento principal
        Departamento existing = departamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento", id));
        existing.setEstado(EstadoDepartamento.INACTIVO);
        departamentoRepository.save(existing);

        // 2. Actualización Masiva en Cascada

        // Desactivar todas las provincias de este departamento
        provinciaRepository.actualizarEstadoPorDepartamento(id, EstadoProvincia.INACTIVO);

        // Desactivar todos los distritos de este departamento
        distritoRepository.actualizarEstadoPorDepartamento(id, EstadoDistrito.INACTIVO);

        // Desactivar todas las tarifas asociadas a este departamento
        tarifaEnvioRepository.actualizarEstadoPorDepartamento(id, EstadoTarifa.INACTIVO);
    }

    @Override
    @Transactional
    public void activar(Long id) {
        Departamento existing = departamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento", id));
        existing.setEstado(EstadoDepartamento.ACTIVO);
        departamentoRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!departamentoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Departamento", id);
        }
        if (provinciaRepository.existsByDepartamento_Id(id)) {
            throw new IllegalStateException("No se puede eliminar un departamento con provincias asociadas.");
        }

        departamentoRepository.deleteById(id);
    }
}
