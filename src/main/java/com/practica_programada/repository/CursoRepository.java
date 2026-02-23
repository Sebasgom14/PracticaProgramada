package com.practica_programada.repository;

import com.practica_programada.domain.Curso;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Integer> {

    List<Curso> findByEstadoTrue();

}
