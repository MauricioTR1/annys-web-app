package com.annysweb.backend.controller;

import com.annysweb.backend.entity.Usuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.annysweb.backend.service.UsuariosService;

import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuariosController {
    @Autowired
    private final UsuariosService usuariosService;
    public UsuariosController(UsuariosService usuariosService) {
        this.usuariosService = usuariosService;
    }
    @GetMapping("/{id_usuarios}")
    public Optional<Usuarios> getById(@PathVariable("id_usuarios") Integer id){
        return usuariosService.getUsuario(id);
    }
    @PostMapping
    public void saveUpdate(@RequestBody Usuarios usuario){
        usuariosService.saveOrUpdate(usuario);
    }
    @DeleteMapping("/{id_usuarios}")
    public void saveDelete(@PathVariable("id_usuarios") Integer id){
        usuariosService.delete(id);
    }
}
