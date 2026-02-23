package com.practica_programada.controller;

import com.practica_programada.domain.Estudiante;
import com.practica_programada.service.CursoService;
import com.practica_programada.service.EstudianteService;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/estudiante")
public class EstudianteController {

    @Autowired
    private EstudianteService estudianteService;

    @Autowired
    private CursoService cursoService;

    @GetMapping("/listado")
    public String listado(@RequestParam(required = false) Integer idCurso, Model model) {
        var estudiantes = (idCurso != null)
                ? estudianteService.getEstudiantesPorCurso(idCurso)
                : estudianteService.getEstudiantes();

        model.addAttribute("estudiantes", estudiantes);
        model.addAttribute("totalEstudiantes", estudiantes.size());

        // Para el filtro (puede ser todos, activos e inactivos)
        model.addAttribute("cursos", cursoService.getCursos(false));

        // Para matrícula (solo activos)
        model.addAttribute("cursosActivos", cursoService.getCursos(true));

        model.addAttribute("idCursoSeleccionado", idCurso);
        return "estudiante/listado";
    }

    @GetMapping("/modificar/{idEstudiante}")
    public String modificar(@PathVariable Integer idEstudiante,
            Model model,
            RedirectAttributes redirectAttributes) {

        Optional<Estudiante> estudianteOpt = estudianteService.getEstudiante(idEstudiante);

        if (estudianteOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El estudiante no fue encontrado.");
            return "redirect:/estudiante/listado";
        }

        model.addAttribute("estudiante", estudianteOpt.get());
        model.addAttribute("cursos", cursoService.getCursos(true));
        return "estudiante/modifica";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Estudiante estudiante,
            RedirectAttributes redirectAttributes) {

        estudianteService.save(estudiante);
        redirectAttributes.addFlashAttribute("todoOk", "Estudiante guardado correctamente.");
        return "redirect:/estudiante/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idEstudiante,
            RedirectAttributes redirectAttributes) {

        String titulo = "todoOk";
        String mensaje = "Estudiante eliminado correctamente.";

        try {
            estudianteService.delete(idEstudiante);
        } catch (IllegalArgumentException e) {
            titulo = "error";
            mensaje = "El estudiante no existe.";
        } catch (IllegalStateException e) {
            titulo = "error";
            mensaje = "No se puede eliminar el estudiante. Tiene datos asociados.";
        } catch (Exception e) {
            titulo = "error";
            mensaje = "Ocurrió un error al eliminar el estudiante.";
        }

        redirectAttributes.addFlashAttribute(titulo, mensaje);
        return "redirect:/estudiante/listado";
    }

    @GetMapping("/estudiante/listado")
    public String listado(Model model,
            @RequestParam(required = false) String idCurso,
            @RequestParam(required = false) String error) {

        Integer idCursoSeleccionado = null;

        // Convierte idCurso solo si viene con número
        if (idCurso != null && !idCurso.isBlank()) {
            try {
                idCursoSeleccionado = Integer.valueOf(idCurso);
            } catch (NumberFormatException ex) {
                idCursoSeleccionado = null;
                model.addAttribute("mensajeError", "Curso inválido.");
            }
        }

        model.addAttribute("cursos", cursoService.getCursos(true));
        model.addAttribute("idCursoSeleccionado", idCursoSeleccionado);

        if (idCursoSeleccionado != null) {
            model.addAttribute("estudiantes", estudianteService.getEstudiantesPorCurso(idCursoSeleccionado));
        } else {
            model.addAttribute("estudiantes", estudianteService.getEstudiantes());
        }

        if (error != null) {
            model.addAttribute("mensajeError", error);
        }

        // importante: este template es el que te está fallando
        return "estudiante/listado";
    }
}
