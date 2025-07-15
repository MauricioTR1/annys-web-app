package com.annysweb.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="usuarios")
@NoArgsConstructor
@AllArgsConstructor
public class Usuarios {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) // las llaves son autogeneradas en mysql, por eso identity
    @Column(name="id_usuarios", unique=true, nullable=false)
    private Integer id_usuario;

    @NotBlank(message="Nombre de usuario obligatorio")
    @Size(max=100, message="El nombre de usuario no debe exceder 25 caracteres")
    @Column(name="nombre_usuario", nullable = false, length = 100)
    private String nombre_usuario;

    @NotBlank(message="Correo de usuario obligatorio")
    @Size(max=100, message = "El correo no debe exceder 100 caracteres")
    @Column(name="correo_usuario", nullable = false, length = 100)
    private String correo_usuario;

    // por ahorita solamente se guardan las contraseñas como texto plano
    // lo ideal sería encriptar las contraseñas xd
    @NotBlank(message="La contraseña del usuario es obligatorio")
    @Size(max=100, message = "La contraseña no debe exceder 100 caracteres")
    @Column(name="contrasenia_usuario", nullable = false, length = 100)
    private String contrasenia_usuario;
}
