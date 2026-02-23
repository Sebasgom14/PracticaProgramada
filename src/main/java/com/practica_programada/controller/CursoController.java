package com.practica_programada.controller;

import com.practica_programada.domain.Curso;
import com.practica_programada.service.CursoService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/curso")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping("/listado")
    public String listado(Model model) {
        var cursos = cursoService.getCursos(false);
        model.addAttribute("cursos", cursos);
        model.addAttribute("totalCursos", cursos.size());
        return "curso/listado";
    }

    @GetMapping("/modificar/{idCurso}")
    public String modificar(@PathVariable Integer idCurso,
                            Model model,
                            RedirectAttributes redirectAttributes) {

        Optional<Curso> cursoOpt = cursoService.getCurso(idCurso);

        if (cursoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El curso no fue encontrado.");
            return "redirect:/curso/listado";
        }

        model.addAttribute("curso", cursoOpt.get());
        return "curso/modifica";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Curso curso,
                          @RequestParam(required = false) MultipartFile imagenFile,
                          RedirectAttributes redirectAttributes) {

        cursoService.save(curso, imagenFile);
        redirectAttributes.addFlashAttribute("todoOk", "Curso guardado correctamente.");
        return "redirect:/curso/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idCurso,
                           RedirectAttributes redirectAttributes) {

        String titulo = "todoOk";
        String mensaje = "Curso eliminado correctamente.";

        try {
            cursoService.delete(idCurso);
        } catch (IllegalArgumentException e) {
            titulo = "error";
            mensaje = "El curso no existe.";
        } catch (IllegalStateException e) {
            titulo = "error";
            mensaje = "No se puede eliminar el curso porque tiene estudiantes asociados.";
        } catch (Exception e) {
            titulo = "error";
            mensaje = "Ocurrió un error al eliminar el curso.";
        }

        redirectAttributes.addFlashAttribute(titulo, mensaje);
        return "redirect:/curso/listado";
    }
}
