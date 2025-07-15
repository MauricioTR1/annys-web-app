package com.annysweb.backend.security;

import com.annysweb.backend.entity.Usuarios;
import com.annysweb.backend.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuariosRepository usuariosRepository;

    @Autowired
    public UserDetailsServiceImpl(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuarios usuario = usuariosRepository.findByCorreoUsuario(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + email));

        return new org.springframework.security.core.userdetails.User(
                usuario.getCorreoUsuario(),
                usuario.getContraseniaUsuario(),
                new ArrayList<>()
        );
    }
}
