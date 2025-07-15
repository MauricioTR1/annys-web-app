package com.annysweb.backend.repository;

import com.annysweb.backend.entity.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {
    Optional<Usuarios> findByCorreoUsuario(String correoUsuario);

    Optional<Usuarios> findByNombreUsuario(String nombreUsuario);
}
