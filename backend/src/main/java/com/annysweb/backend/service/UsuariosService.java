package com.annysweb.backend.service;

import com.annysweb.backend.entity.Usuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.annysweb.backend.repository.UsuariosRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UsuariosService {
    @Autowired
    UsuariosRepository usuariosRepository;

    public List<Usuarios> getUsuarios() { return usuariosRepository.findAll(); }

    public Optional<Usuarios> getUsuario(Integer id_usuario) { return usuariosRepository.findById(id_usuario); }

    public void saveOrUpdate(Usuarios usuario) { usuariosRepository.save(usuario); }

    public void delete(Integer id_usuario) { usuariosRepository.deleteById(id_usuario); }
}
