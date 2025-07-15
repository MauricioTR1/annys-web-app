package com.annysweb.backend.controller;

import com.annysweb.backend.entity.Usuarios;
import com.annysweb.backend.service.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid; // Importar para validación

import java.util.List; // Para retornar lista de usuarios

@RestController
@RequestMapping("/api/usuarios") // Prefijo de URL para todos los endpoints de este controlador
// @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:5500", "http://localhost:3000"}) // Ya manejado por CorsConfig global
public class UsuariosController {

    // Inyección de dependencias por constructor (práctica recomendada)
    private final UsuariosService usuariosService;

    @Autowired
    public UsuariosController(UsuariosService usuariosService) {
        this.usuariosService = usuariosService;
    }

    // --- Endpoint para OBTENER todos los usuarios (GET /api/usuarios) ---
    // Este endpoint debería estar protegido y posiblemente solo accesible por administradores.
    @GetMapping
    public ResponseEntity<List<Usuarios>> getAllUsuarios() {
        List<Usuarios> usuarios = usuariosService.getUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    // --- Endpoint para OBTENER un usuario por ID (GET /api/usuarios/{id}) ---
    // Este endpoint podría ser para que un usuario vea su propio perfil o para que un admin vea cualquier usuario.
    @GetMapping("/{id}") // Usamos 'id' como nombre de la variable de path
    public ResponseEntity<Usuarios> getUsuarioById(@PathVariable("id") Long id) {
        return usuariosService.getUsuario(id)
                .map(ResponseEntity::ok) // Si se encuentra, retorna 200 OK con el usuario
                .orElse(ResponseEntity.notFound().build()); // Si no se encuentra, retorna 404 Not Found
    }

    // --- Endpoint para CREAR un nuevo usuario (POST /api/usuarios) ---
    // NOTA: Este endpoint no es para el REGISTRO público. El REGISTRO público debe usar /api/auth/register.
    // Este POST podría ser para que un ADMINISTRADOR cree un usuario directamente.
    // La contraseña se hashea en el UsuariosService.
    @PostMapping
    public ResponseEntity<Usuarios> createUsuario(@Valid @RequestBody Usuarios usuario) {
        // Antes de guardar, podrías añadir más lógica de negocio, como verificar si el correo/nombre ya existen
        // aunque UsuariosService.saveOrUpdate ya hashea la contraseña.
        // Las validaciones de unicidad deberían estar en el servicio o en el AuthController para registro.
        Usuarios nuevoUsuario = usuariosService.saveOrUpdate(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario); // Retorna 201 Created con el usuario creado
    }

    // --- Endpoint para ACTUALIZAR un usuario existente (PUT /api/usuarios/{id}) ---
    // Este endpoint es para actualizar un usuario por su ID.
    // La contraseña se hashea en el UsuariosService si viene en el request.
    @PutMapping("/{id}")
    public ResponseEntity<Usuarios> updateUsuario(@PathVariable("id") Long id, @Valid @RequestBody Usuarios usuarioDetalles) {
        // Asegurarse de que el ID del path coincida con el ID del cuerpo si se envía
        if (usuarioDetalles.getIdUsuarios() == null || !usuarioDetalles.getIdUsuarios().equals(id)) {
            usuarioDetalles.setIdUsuarios(id); // Asegura que el ID del objeto coincida con el del path
        }

        // Buscar el usuario existente
        return usuariosService.getUsuario(id)
                .map(usuarioExistente -> {
                    // Actualizar solo los campos permitidos o que vengan en el request
                    usuarioExistente.setNombreUsuario(usuarioDetalles.getNombreUsuario());
                    usuarioExistente.setCorreoUsuario(usuarioDetalles.getCorreoUsuario());
                    // IMPORTANTE: Si la contraseña viene en usuarioDetalles, se hasheará de nuevo en saveOrUpdate.
                    // Para una actualización de contraseña separada o condicional, se requiere lógica adicional.
                    usuarioExistente.setContraseniaUsuario(usuarioDetalles.getContraseniaUsuario()); // Se hasheará en el servicio

                    Usuarios usuarioActualizado = usuariosService.saveOrUpdate(usuarioExistente);
                    return ResponseEntity.ok(usuarioActualizado); // Retorna 200 OK con el usuario actualizado
                })
                .orElse(ResponseEntity.notFound().build()); // Si no se encuentra, retorna 404 Not Found
    }

    // --- Endpoint para ELIMINAR un usuario (DELETE /api/usuarios/{id}) ---
    // Este endpoint debería estar protegido y posiblemente solo accesible por administradores.
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Retorna 204 No Content para indicar éxito sin contenido en la respuesta
    public void deleteUsuario(@PathVariable("id") Long id) {
        usuariosService.delete(id); // Llama al método delete del servicio
    }
}