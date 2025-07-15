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
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_usuarios")
    private Long idUsuarios;

    @NotBlank(message="Nombre de usuario obligatorio")
    @Size(max=100, message="El nombre de usuario no debe exceder 100 caracteres")
    @Column(name="nombre_usuario", nullable = false, length = 100)
    private String nombreUsuario;

    @NotBlank(message="Correo de usuario obligatorio")
    @Size(max=100, message = "El correo no debe exceder 100 caracteres")
    @Column(name="correo_usuario", nullable = false, length = 100, unique = true)
    private String correoUsuario;

    @NotBlank(message="La contraseña del usuario es obligatorio")
    @Size(max=100, message = "La contraseña no debe exceder 100 caracteres")
    @Column(name="contrasenia_usuario", nullable = false, length = 100)
    private String contraseniaUsuario;
}
