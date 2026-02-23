package com.practica_programada.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Entity
@Table(name = "estudiante")
public class Estudiante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estudiante")
    private Integer idEstudiante;

    @Column(nullable = false, length = 120)
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 120, message = "El nombre no puede tener más de 120 caracteres.")
    private String nombre;

    @Column(nullable = false, unique = true, length = 180)
    @NotBlank(message = "El correo no puede estar vacío.")
    @Email(message = "El correo no tiene un formato válido.")
    @Size(max = 180, message = "El correo no puede tener más de 180 caracteres.")
    private String correo;

    @Column(nullable = false)
    @NotNull(message = "La edad no puede estar vacía.")
    @Min(value = 0, message = "La edad debe ser mayor o igual a 0.")
    @Max(value = 120, message = "La edad no puede superar 120.")
    private Integer edad;

    @ManyToOne
    @JoinColumn(name = "id_curso")
    private Curso curso;

}
