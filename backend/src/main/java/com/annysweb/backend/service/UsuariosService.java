package com.annysweb.backend.service;

import com.annysweb.backend.entity.Usuarios;
import com.annysweb.backend.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Importar PasswordEncoder
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importar Transactional

import java.util.List;
import java.util.Optional;

@Service
public class UsuariosService {

    private final UsuariosRepository usuariosRepository;
    private final PasswordEncoder passwordEncoder; // Inyectar PasswordEncoder

    @Autowired
    public UsuariosService(UsuariosRepository usuariosRepository, PasswordEncoder passwordEncoder) {
        this.usuariosRepository = usuariosRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<Usuarios> getUsuarios() {
        return usuariosRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Usuarios> getUsuario(Long idUsuario) { // Cambiado a Long para el ID
        return usuariosRepository.findById(idUsuario);
    }

    @Transactional(readOnly = true)
    // Método para buscar un usuario por su correo electrónico
    public Optional<Usuarios> findByCorreoUsuario(String correo) {
        return usuariosRepository.findByCorreoUsuario(correo);
    }

    @Transactional(readOnly = true)
    // Método para buscar un usuario por su nombre de usuario
    public Optional<Usuarios> findByNombreUsuario(String nombreUsuario) {
        return usuariosRepository.findByNombreUsuario(nombreUsuario);
    }

    @Transactional // Marcar como transaccional para operaciones de escritura
    public Usuarios saveOrUpdate(Usuarios usuario) {
        // Hashea la contraseña antes de guardar o actualizar
        usuario.setContraseniaUsuario(passwordEncoder.encode(usuario.getContraseniaUsuario()));
        return usuariosRepository.save(usuario);
    }

    @Transactional // Marcar como transaccional para operaciones de escritura
    public void delete(Long idUsuario) { // Cambiado a Long para el ID
        usuariosRepository.deleteById(idUsuario);
    }
}
