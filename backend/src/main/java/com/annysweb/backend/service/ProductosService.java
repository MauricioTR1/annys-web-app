package com.annysweb.backend.service;

import com.annysweb.backend.entity.Productos;
import com.annysweb.backend.repository.ProductosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductosService {
    @Autowired
    private ProductosRepository productosRepository;
    public List<Productos> getProductos() {
        return productosRepository.findAll();
    }
    public Optional<Productos> getProducto(String id_producto) {
        return productosRepository.findById(id_producto);
    }

    public void saveOrUpdate(Productos producto) {
        productosRepository.save(producto);
    }
    public void delete(String codigo_producto) {
        productosRepository.deleteById(codigo_producto);
    }
}
