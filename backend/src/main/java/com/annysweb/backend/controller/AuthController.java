package com.annysweb.backend.controller;
import com.annysweb.backend.dto.LoginRequest;
import com.annysweb.backend.dto.JwtResponse;
import com.annysweb.backend.entity.Usuarios; // Usa tu entidad Usuarios
import com.annysweb.backend.security.JwtUtil;
import com.annysweb.backend.service.UsuariosService; // Usa tu UsuariosService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UsuariosService usuariosService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtUtil jwtUtil, UsuariosService usuariosService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.usuariosService = usuariosService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody LoginRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername()));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registerUser(@Valid @RequestBody Usuarios usuario) {
        try {
            if (usuariosService.findByCorreoUsuario(usuario.getCorreoUsuario()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya está registrado.");
            }
            if (usuariosService.findByNombreUsuario(usuario.getNombreUsuario()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("El nombre de usuario ya existe.");
            }
            Usuarios nuevoUsuario = usuariosService.saveOrUpdate(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar usuario: " + e.getMessage());
        }
    }
}