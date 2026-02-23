package com.practica_programada.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.Setter;
import lombok.Getter;

@Data
@Getter
@Setter
@Entity
@Table(name = "curso")
public class Curso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso")
    private Integer idCurso;

    @Column(nullable = false, length = 120)
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 120, message = "El nombre no puede tener más de 120 caracteres.")
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    @NotNull(message = "Los créditos no pueden estar vacíos.")
    @Min(value = 0, message = "Los créditos deben ser mayores o iguales a 0.")
    @Max(value = 30, message = "Los créditos no pueden superar 30.")
    private Integer creditos;

    @Column(nullable = false)
    private Boolean estado;

    @Column(columnDefinition = "TEXT")
    private String imagen;

    @OneToMany(mappedBy = "curso")
    private List<Estudiante> estudiantes;

    public Integer getIdCurso() {
        return idCurso;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

}
