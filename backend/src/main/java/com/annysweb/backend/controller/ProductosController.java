package com.annysweb.backend.controller;

import com.annysweb.backend.entity.Productos;
import com.annysweb.backend.service.ProductosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("productos")
public class ProductosController {
    @Autowired
    private final ProductosService productosService;
    public ProductosController(ProductosService productosService) {
        this.productosService = productosService;
    }
    @GetMapping("/todos_los_productos")
    public List<Productos> getTodosLosProductos() {
        return productosService.getProductos();
    }
    @GetMapping("/{codigo_producto}")
    public Optional<Productos> getById(@PathVariable("codigo_producto") String codigo_producto) {
        return productosService.getProducto(codigo_producto);
    }

    @PostMapping
    public void saveUpdate(@RequestBody Productos productos) {
        productosService.saveOrUpdate(productos);
    }

    @DeleteMapping("/{codigo_producto}")
    public void saveDelete(@PathVariable("codigo_producto") String codigo_producto) {
        productosService.delete(codigo_producto);
    }
}
