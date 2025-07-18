package com.annysweb.backend.controller;

import com.annysweb.backend.entity.Proveedores;
import com.annysweb.backend.service.ProveedoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
// @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:5500", "http://localhost:3000"})
public class ProveedoresController {

    private final ProveedoresService proveedoresService;

    @Autowired
    public ProveedoresController(ProveedoresService proveedoresService) {
        this.proveedoresService = proveedoresService;
    }

    // --- Endpoint para CREAR un nuevo proveedor (POST /api/proveedores) ---
    @PostMapping
    public ResponseEntity<Proveedores> createProveedor(@Valid @RequestBody Proveedores proveedor) {
        Proveedores newProveedor = proveedoresService.saveProveedor(proveedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProveedor); // Retorna 201 Created
    }

    // --- Endpoint para LEER todos los proveedores (GET /api/proveedores) ---
    @GetMapping
    public ResponseEntity<List<Proveedores>> getAllProveedores() {
        List<Proveedores> proveedores = proveedoresService.getAllProveedores();
        return ResponseEntity.ok(proveedores); // Retorna 200 OK con la lista de proveedores
    }

    // --- Endpoint para LEER un proveedor por su RFC (GET /api/proveedores/{rfc}) ---
    @GetMapping("/{rfc}")
    public ResponseEntity<Proveedores> getProveedorByRfc(@PathVariable String rfc) {
        return proveedoresService.getProveedorByRfc(rfc)
                .map(ResponseEntity::ok) // Si lo encuentra, retorna 200 OK y el proveedor
                .orElse(ResponseEntity.notFound().build()); // Si no, retorna 404 Not Found
    }

    // --- Endpoint para ACTUALIZAR un proveedor (PUT /api/proveedores/{rfc}) ---
    @PutMapping("/{rfc}")
    public ResponseEntity<Proveedores> updateProveedor(@PathVariable String rfc, @Valid @RequestBody Proveedores proveedorDetalles) {
        try {
            Proveedores updatedProveedor = proveedoresService.updateProveedor(rfc, proveedorDetalles);
            return ResponseEntity.ok(updatedProveedor); // Retorna 200 OK con el proveedor actualizado
        } catch (RuntimeException e) {
            // Maneja la excepción si el proveedor no se encuentra
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // --- Endpoint para BORRAR un proveedor (DELETE /api/proveedores/{rfc}) ---
    @DeleteMapping("/{rfc}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Retorna 204 No Content si se elimina correctamente
    public void deleteProveedor(@PathVariable String rfc) {
        try {
            proveedoresService.deleteProveedor(rfc);
        } catch (RuntimeException e) {
            throw e;
        }
    }
}