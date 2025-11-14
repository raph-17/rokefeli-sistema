package com.rokefeli.colmenares.api.service.impl;

import com.rokefeli.colmenares.api.dto.create.DepartamentoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.DepartamentoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.DepartamentoUpdateDTO;
import com.rokefeli.colmenares.api.entity.Departamento;
import com.rokefeli.colmenares.api.entity.Distrito;
import com.rokefeli.colmenares.api.entity.Provincia;
import com.rokefeli.colmenares.api.entity.enums.EstadoDepartamento;
import com.rokefeli.colmenares.api.entity.enums.EstadoDistrito;
import com.rokefeli.colmenares.api.entity.enums.EstadoProvincia;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.DepartamentoMapper;
import com.rokefeli.colmenares.api.repository.DepartamentoRepository;
import com.rokefeli.colmenares.api.repository.DistritoRepository;
import com.rokefeli.colmenares.api.repository.ProvinciaRepository;
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
    @Transactional
    public DepartamentoResponseDTO create(DepartamentoCreateDTO createDTO) {
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
        Departamento existing = departamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento", id));
        existing.setEstado(EstadoDepartamento.INACTIVO);
        departamentoRepository.save(existing);

        List<Provincia> provincias = provinciaRepository.findByDepartamento_Id(id);

        provincias.forEach(p -> p.setEstado(EstadoProvincia.INACTIVO));
        provinciaRepository.saveAll(provincias);

        for (Provincia p : provincias) {
            List<Distrito> distritos = distritoRepository.findByProvincia_Id(p.getId());
            distritos.forEach(d -> d.setEstado(EstadoDistrito.INACTIVO));
            distritoRepository.saveAll(distritos);
        }
    }

    @Override
    @Transactional
    public void activar(Long id) {
        Departamento existing = departamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento", id));
        existing.setEstado(EstadoDepartamento.ACTIVO);
        departamentoRepository.save(existing);

        List<Provincia> provincias = provinciaRepository.findByDepartamento_Id(id);

        provincias.forEach(p -> p.setEstado(EstadoProvincia.ACTIVO));
        provinciaRepository.saveAll(provincias);

        for (Provincia p : provincias) {
            List<Distrito> distritos = distritoRepository.findByProvincia_Id(p.getId());
            distritos.forEach(d -> d.setEstado(EstadoDistrito.ACTIVO));
            distritoRepository.saveAll(distritos);
        }
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
