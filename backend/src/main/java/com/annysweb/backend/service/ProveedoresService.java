package com.annysweb.backend.service;

import com.annysweb.backend.entity.Proveedores;
import com.annysweb.backend.repository.ProveedoresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedoresService {

    private final ProveedoresRepository proveedoresRepository;

    @Autowired
    public ProveedoresService(ProveedoresRepository proveedoresRepository) {
        this.proveedoresRepository = proveedoresRepository;
    }

    // --- Métodos para LEER proveedores ---

    @Transactional(readOnly = true)
    public List<Proveedores> getAllProveedores() {
        return proveedoresRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Proveedores> getProveedorByRfc(String rfc) {
        return proveedoresRepository.findById(rfc);
    }

    // --- Métodos para CREAR y ACTUALIZAR proveedores ---

    @Transactional
    public Proveedores saveProveedor(Proveedores proveedor) {
        System.out.println("Recibiendo proveedor en saveProveedor: " + proveedor);
        return proveedoresRepository.save(proveedor);
    }

    @Transactional
    public Proveedores updateProveedor(String rfc, Proveedores proveedorDetalles) {
        return proveedoresRepository.findById(rfc)
                .map(proveedorExistente -> {
                    proveedorExistente.setNombre_proveedor(proveedorDetalles.getNombre_proveedor());
                    return proveedoresRepository.save(proveedorExistente);
                })
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con RFC: " + rfc));
    }

    // --- Método para BORRAR proveedores ---

    @Transactional
    public void deleteProveedor(String rfc) {
        if (!proveedoresRepository.existsById(rfc)) {
            throw new RuntimeException("Proveedor no encontrado con RFC: " + rfc);
        }
        proveedoresRepository.deleteById(rfc);
    }
}
