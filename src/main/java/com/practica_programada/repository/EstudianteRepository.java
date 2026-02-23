package com.practica_programada.repository;

import com.practica_programada.domain.Estudiante;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {

    List<Estudiante> findByCurso_IdCurso(Integer idCurso);

}
