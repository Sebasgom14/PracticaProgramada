package com.practica_programada.service;

import com.practica_programada.domain.Curso;
import com.practica_programada.repository.CursoRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @Transactional(readOnly = true)
    public List<Curso> getCursos(boolean soloActivos) {
        if (soloActivos) {
            return cursoRepository.findByEstadoTrue();
        }
        return cursoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Curso> getCurso(Integer idCurso) {
        return cursoRepository.findById(idCurso);
    }

    @Transactional
    public void save(Curso curso, MultipartFile imagenFile) {
        curso = cursoRepository.save(curso);

        if (imagenFile != null && !imagenFile.isEmpty()) {
            try {
                String rutaImagen = firebaseStorageService.uploadImage(
                        imagenFile, "curso", curso.getIdCurso());
                curso.setImagen(rutaImagen);
                cursoRepository.save(curso);
            } catch (IOException e) {
                // log error si se requiere
            }
        }
    }

    @Transactional
    public void delete(Integer idCurso) {
        if (!cursoRepository.existsById(idCurso)) {
            throw new IllegalArgumentException("El curso con ID " + idCurso + " no existe.");
        }
        try {
            cursoRepository.deleteById(idCurso);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException(
                    "No se puede eliminar el curso. Tiene estudiantes asociados.", e);
        }
    }
}
