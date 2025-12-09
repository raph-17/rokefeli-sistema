package com.rokefeli.colmenares.api.service.impl;

import com.rokefeli.colmenares.api.dto.create.DistritoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.DistritoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.DistritoUpdateDTO;
import com.rokefeli.colmenares.api.entity.Distrito;
import com.rokefeli.colmenares.api.entity.Provincia;
import com.rokefeli.colmenares.api.entity.enums.EstadoDistrito;
import com.rokefeli.colmenares.api.entity.enums.EstadoProvincia;
import com.rokefeli.colmenares.api.entity.enums.EstadoTarifa;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.DistritoMapper;
import com.rokefeli.colmenares.api.repository.DistritoRepository;
import com.rokefeli.colmenares.api.repository.ProvinciaRepository;
import com.rokefeli.colmenares.api.repository.TarifaEnvioRepository;
import com.rokefeli.colmenares.api.service.interfaces.DistritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DistritoServiceImpl implements DistritoService {

    @Autowired
    private DistritoRepository distritoRepository;

    @Autowired
    private ProvinciaRepository provinciaRepository;

    @Autowired
    private TarifaEnvioRepository tarifaEnvioRepository;

    @Autowired
    private DistritoMapper mapper;

    @Override
    public List<DistritoResponseDTO> findAll() {
        return distritoRepository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<DistritoResponseDTO> findByProvinciaId(Long id) {
        return distritoRepository.findByProvincia_Id(id)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<DistritoResponseDTO> findByProvinciaIdActivos(Long id) {
        return distritoRepository.findByProvincia_IdAndEstado(id, EstadoDistrito.ACTIVO)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public DistritoResponseDTO findById(Long id) {
        Distrito existing = distritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Distrito", id));
        return mapper.toResponseDTO(existing);
    }

    @Override
    public List<DistritoResponseDTO> findByEstado(EstadoDistrito estado) {
        return distritoRepository.findByEstado(estado)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public DistritoResponseDTO create(DistritoCreateDTO createDTO) {
        if (distritoRepository.existsByNombreIgnoreCaseAndProvincia_Id(createDTO.getNombre(), createDTO.getIdProvincia())) {
            throw new IllegalArgumentException("Ya existe un distrito con el mismo nombre en la provincia especificada.");
        }
        Provincia provincia = provinciaRepository.findByIdAndEstado(createDTO.getIdProvincia(), EstadoProvincia.ACTIVO).
                orElseThrow(() -> new ResourceNotFoundException("Provincia", createDTO.getIdProvincia()));

        Distrito distrito = mapper.toEntity(createDTO);
        distrito.setProvincia(provincia);
        distrito.setEstado(EstadoDistrito.ACTIVO);
        return mapper.toResponseDTO(distritoRepository.save(distrito));
    }


    @Override
    @Transactional
    public DistritoResponseDTO update(Long id, DistritoUpdateDTO updateDTO) {
        Distrito existing = distritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Distrito", id));

        Provincia provincia = provinciaRepository.findByIdAndEstado(updateDTO.getIdProvincia(), EstadoProvincia.ACTIVO)
                .orElseThrow(() -> new ResourceNotFoundException("Provincia", updateDTO.getIdProvincia()));

        if (distritoRepository.existsByNombreIgnoreCaseAndProvincia_IdAndIdNot(updateDTO.getNombre(), updateDTO.getIdProvincia(), id)) {
            throw new IllegalArgumentException("Ya existe un distrito con ese nombre en esta provincia.");
        }
        
        mapper.updateEntityFromDTO(updateDTO, existing);
        existing.setProvincia(provincia);
        Distrito updated = distritoRepository.save(existing);
        return mapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void desactivar(Long id) {
        // 1. Desactivar el Distrito
        Distrito existing = distritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Distrito", id));
        existing.setEstado(EstadoDistrito.INACTIVO);
        distritoRepository.save(existing);

        // 2. Desactivar las Tarifas asociadas
        tarifaEnvioRepository.actualizarEstadoPorDistrito(id, EstadoTarifa.INACTIVO);
    }

    @Override
    @Transactional
    public void activar(Long id) {
        // 1. Buscar distrito
        Distrito existing = distritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Distrito", id));

        // 2. Obtener el padre
        Provincia padre = existing.getProvincia();

        // 3. Validaciones
        if (padre == null) {
            throw new IllegalStateException("El Distrito no tiene un departamento asignado.");
        }

        if (padre.getEstado() == EstadoProvincia.INACTIVO) {
            throw new IllegalArgumentException("No es posible activar un distrito de una provincia inactiva. Active la provincia primero.");
        }

        // 4. Activar y Guardar
        existing.setEstado(EstadoDistrito.ACTIVO);
        distritoRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!distritoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Distrito", id);
        }
        distritoRepository.deleteById(id);
    }
}
