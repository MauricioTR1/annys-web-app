package com.annysweb.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="categorias")
@NoArgsConstructor
@AllArgsConstructor
public class Categorias {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer id_categoria;

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 25, message = "El nombre de la categoría no debe exceder los 25 caracteres")
    @Column(name = "nombre_categoria", nullable = false, length = 25)
    private String nombre_categoria;

    @Size(max = 200, message = "La descripción de la categoría no debe exceder los 30 caracteres")
    @Column(name = "descripcion_categoria", length = 200)
    private String descripcion_categoria;
}