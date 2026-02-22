package com.practica_programada.service;

import com.practica_programada.domain.Estudiante;
import com.practica_programada.repository.EstudianteRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Transactional(readOnly = true)
    public List<Estudiante> getEstudiantes() {
        return estudianteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Estudiante> getEstudiantesPorCurso(Integer idCurso) {
        return estudianteRepository.findByCurso_IdCurso(idCurso);
    }

    @Transactional(readOnly = true)
    public Optional<Estudiante> getEstudiante(Integer idEstudiante) {
        return estudianteRepository.findById(idEstudiante);
    }

    @Transactional
    public void save(Estudiante estudiante) {
        estudianteRepository.save(estudiante);
    }

    @Transactional
    public void delete(Integer idEstudiante) {
        if (!estudianteRepository.existsById(idEstudiante)) {
            throw new IllegalArgumentException("El estudiante con ID " + idEstudiante + " no existe.");
        }
        try {
            estudianteRepository.deleteById(idEstudiante);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException(
                    "No se puede eliminar el estudiante. Tiene datos asociados.", e);
        }
    }
}
