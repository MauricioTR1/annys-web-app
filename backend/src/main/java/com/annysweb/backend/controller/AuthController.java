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
@RequestMapping("/api/auth") // Prefijo para endpoints de autenticación
@CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:5500", "http://localhost:3000"}) // Permite solicitudes desde tu frontend HTML/JS
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UsuariosService usuariosService; // Tu servicio de usuarios

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtUtil jwtUtil, UsuariosService usuariosService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.usuariosService = usuariosService;
    }

    // Endpoint para el login
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody LoginRequest authenticationRequest) throws Exception {
        try {
            // Autentica al usuario usando el correo (username) y la contraseña
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            // Si las credenciales son inválidas, devuelve un error 401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }

        // Si la autenticación es exitosa, carga los detalles del usuario por su correo
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        // Genera el JWT
        final String jwt = jwtUtil.generateToken(userDetails);

        // Retorna el JWT y el correo del usuario en la respuesta
        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername())); // userDetails.getUsername() aquí es el correo
    }

    // Endpoint para el registro de usuarios
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED) // Devuelve 201 Created si el registro es exitoso
    public ResponseEntity<?> registerUser(@Valid @RequestBody Usuarios usuario) {
        try {
            // Verifica si el correo o nombre de usuario ya existen para evitar duplicados
            if (usuariosService.findByCorreoUsuario(usuario.getCorreoUsuario()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya está registrado.");
            }
            if (usuariosService.findByNombreUsuario(usuario.getNombreUsuario()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("El nombre de usuario ya existe.");
            }

            // Guarda el nuevo usuario (la contraseña será hasheada en el servicio)
            Usuarios nuevoUsuario = usuariosService.saveOrUpdate(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (Exception e) {
            // Manejo genérico de errores, puedes refinarlo
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar usuario: " + e.getMessage());
        }
    }
}