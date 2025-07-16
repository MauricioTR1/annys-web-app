package com.annysweb.backend.controller;

import com.annysweb.backend.entity.Usuarios;
import com.annysweb.backend.service.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/perfil")
public class PerfilController {

    private final UsuariosService usuariosService;

    @Autowired
    public PerfilController(UsuariosService usuariosService) {
        this.usuariosService = usuariosService;
    }

    @GetMapping("/mi-perfil")
    public ResponseEntity<Usuarios> getMyProfile(Principal principal) {
        String userEmail = principal.getName();

        Optional<Usuarios> usuarioOptional = usuariosService.findByCorreoUsuario(userEmail);

        if (usuarioOptional.isPresent()) {
            return ResponseEntity.ok(usuarioOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /*
    @PutMapping("/actualizar")
    public ResponseEntity<Usuarios> updateMyProfile(@Valid @RequestBody Usuarios usuarioDetalles, Principal principal) {
        String userEmail = principal.getName();
        return usuariosService.findByCorreoUsuario(userEmail)
                .map(usuarioExistente -> {
                    usuarioExistente.setNombreUsuario(usuarioDetalles.getNombreUsuario());
                    Usuarios usuarioActualizado = usuariosService.saveOrUpdate(usuarioExistente); // saveOrUpdate ya hashea si se cambia la contraseña
                    return ResponseEntity.ok(usuarioActualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    */
}