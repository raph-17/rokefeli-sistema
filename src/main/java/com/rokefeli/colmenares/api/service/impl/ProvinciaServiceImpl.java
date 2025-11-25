package com.rokefeli.colmenares.api.service.impl;

import com.rokefeli.colmenares.api.dto.create.ProvinciaCreateDTO;
import com.rokefeli.colmenares.api.dto.response.ProvinciaResponseDTO;
import com.rokefeli.colmenares.api.dto.update.ProvinciaUpdateDTO;
import com.rokefeli.colmenares.api.entity.Departamento;
import com.rokefeli.colmenares.api.entity.Provincia;
import com.rokefeli.colmenares.api.entity.enums.EstadoDepartamento;
import com.rokefeli.colmenares.api.entity.enums.EstadoDistrito;
import com.rokefeli.colmenares.api.entity.enums.EstadoProvincia;
import com.rokefeli.colmenares.api.entity.enums.EstadoTarifa;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.ProvinciaMapper;
import com.rokefeli.colmenares.api.repository.DepartamentoRepository;
import com.rokefeli.colmenares.api.repository.DistritoRepository;
import com.rokefeli.colmenares.api.repository.ProvinciaRepository;
import com.rokefeli.colmenares.api.repository.TarifaEnvioRepository;
import com.rokefeli.colmenares.api.service.interfaces.ProvinciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProvinciaServiceImpl implements ProvinciaService {

    @Autowired
    private ProvinciaRepository provinciaRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private DistritoRepository distritoRepository;

    @Autowired
    private TarifaEnvioRepository tarifaEnvioRepository;

    @Autowired
    private ProvinciaMapper mapper;

    @Override
    public List<ProvinciaResponseDTO> findAll() {
        return provinciaRepository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<ProvinciaResponseDTO> findByDepartamentoId(Long id) {
        return provinciaRepository.findByDepartamento_Id(id)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<ProvinciaResponseDTO> findByDepartamentoIdActivos(Long id) {
        return provinciaRepository.findByDepartamento_IdAndEstado(id, EstadoProvincia.ACTIVO)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public ProvinciaResponseDTO findById(Long id) {
        Provincia existing = provinciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provincia", id));
        return mapper.toResponseDTO(existing);
    }

    @Override
    @Transactional
    public ProvinciaResponseDTO create(ProvinciaCreateDTO createDTO) {
        if(provinciaRepository.existsByNombreIgnoreCaseAndDepartamento_Id(createDTO.getNombre(), createDTO.getIdDepartamento())) {
            throw new IllegalStateException("Ya existe una provincia con ese nombre en este departamento.");
        }
        Departamento departamento = departamentoRepository.findByIdAndEstado(createDTO.getIdDepartamento(), EstadoDepartamento.ACTIVO)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento", createDTO.getIdDepartamento()));
        Provincia provincia = mapper.toEntity(createDTO);
        provincia.setEstado(EstadoProvincia.ACTIVO);
        provincia.setDepartamento(departamento);
        Provincia saved = provinciaRepository.save(provincia);
        return mapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public ProvinciaResponseDTO update(Long id, ProvinciaUpdateDTO updateDTO) {
        Provincia existing = provinciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provincia", id));

        Departamento departamento = departamentoRepository.findByIdAndEstado(updateDTO.getIdDepartamento(), EstadoDepartamento.ACTIVO)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento", updateDTO.getIdDepartamento()));

        if (provinciaRepository.existsByNombreIgnoreCaseAndDepartamento_IdAndIdNot(updateDTO.getNombre(), updateDTO.getIdDepartamento(), id)) {
            throw new IllegalArgumentException("Ya existe una provincia con ese nombre en este departamento.");
        }

        mapper.updateEntityFromDTO(updateDTO, existing);
        existing.setDepartamento(departamento);
        Provincia updated = provinciaRepository.save(existing);
        return mapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void desactivar(Long id) {
        // 1. Desactivar la Provincia
        Provincia existing = provinciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provincia", id));
        existing.setEstado(EstadoProvincia.INACTIVO);
        provinciaRepository.save(existing);

        // 2. Desactivar Distritos en cascada
        distritoRepository.actualizarEstadoPorProvincia(id, EstadoDistrito.INACTIVO);

        // 3. Desactivar Tarifas en cascada
        tarifaEnvioRepository.actualizarEstadoPorProvincia(id, EstadoTarifa.INACTIVO);
    }

    @Override
    @Transactional
    public void activar(Long id) {
        // 1. Buscar provincia
        Provincia existing = provinciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provincia", id));

        // 2. Obtener padre
        Departamento padre = existing.getDepartamento();

        // 3. Validaciones
        if (padre == null) {
            throw new IllegalStateException("La provincia no tiene un departamento asignado.");
        }

        if (padre.getEstado() == EstadoDepartamento.INACTIVO) {
            throw new IllegalArgumentException("No es posible activar una provincia de un departamento inactivo. Active el departamento primero.");
        }

        // 4. Activar y Guardar
        existing.setEstado(EstadoProvincia.ACTIVO);
        provinciaRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!provinciaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Provincia", id);
        }
        if (distritoRepository.existsByProvincia_Id(id)) {
            throw new IllegalStateException("No se puede eliminar la provincia porque tiene distritos asociados.");
        }
        provinciaRepository.deleteById(id);
    }
}
