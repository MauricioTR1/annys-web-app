package com.annysweb.backend.controller;

import com.annysweb.backend.entity.Usuarios;
import com.annysweb.backend.service.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuariosController {
    private final UsuariosService usuariosService;

    @Autowired
    public UsuariosController(UsuariosService usuariosService) {
        this.usuariosService = usuariosService;
    }

    // --- Endpoint para OBTENER todos los usuarios (GET /api/usuarios) ---
    @GetMapping
    public ResponseEntity<List<Usuarios>> getAllUsuarios() {
        List<Usuarios> usuarios = usuariosService.getUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    // --- Endpoint para OBTENER un usuario por ID (GET /api/usuarios/{id}) ---
    @GetMapping("/{id}")
    public ResponseEntity<Usuarios> getUsuarioById(@PathVariable("id") Long id) {
        return usuariosService.getUsuario(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // --- Endpoint para CREAR un nuevo usuario (POST /api/usuarios) ---
    // Este endpoint no es para el REGISTRO público. El REGISTRO público debe usar /api/auth/register
    // La contraseña se hashea en el UsuariosService
    @PostMapping
    public ResponseEntity<Usuarios> createUsuario(@Valid @RequestBody Usuarios usuario) {
        Usuarios nuevoUsuario = usuariosService.saveOrUpdate(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario); // Retorna 201 Created con el usuario creado
    }

    // --- Endpoint para ACTUALIZAR un usuario existente (PUT /api/usuarios/{id}) ---
    // Este endpoint es para actualizar un usuario por su ID
    // La contraseña se hashea en el UsuariosService si viene en el request
    @PutMapping("/{id}")
    public ResponseEntity<Usuarios> updateUsuario(@PathVariable("id") Long id, @Valid @RequestBody Usuarios usuarioDetalles) {
        if (usuarioDetalles.getIdUsuarios() == null || !usuarioDetalles.getIdUsuarios().equals(id)) {
            usuarioDetalles.setIdUsuarios(id);
        }

        // Buscar el usuario existente
        return usuariosService.getUsuario(id)
                .map(usuarioExistente -> {
                    // Actualizar solo los campos permitidos o que vengan en el request
                    usuarioExistente.setNombreUsuario(usuarioDetalles.getNombreUsuario());
                    usuarioExistente.setCorreoUsuario(usuarioDetalles.getCorreoUsuario());
                    usuarioExistente.setContraseniaUsuario(usuarioDetalles.getContraseniaUsuario()); // Se hasheará en el servicio

                    Usuarios usuarioActualizado = usuariosService.saveOrUpdate(usuarioExistente);
                    return ResponseEntity.ok(usuarioActualizado); // Retorna 200 OK con el usuario actualizado
                })
                .orElse(ResponseEntity.notFound().build()); // Si no se encuentra, retorna 404 Not Found
    }

    // --- Endpoint para ELIMINAR un usuario (DELETE /api/usuarios/{id}) ---
    // Este endpoint debería estar protegido y posiblemente solo accesible por administradores
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Retorna 204 No Content para indicar éxito sin contenido en la respuesta
    public void deleteUsuario(@PathVariable("id") Long id) {
        usuariosService.delete(id);
    }
}