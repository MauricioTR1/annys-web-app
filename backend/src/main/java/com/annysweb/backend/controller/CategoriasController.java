package com.annysweb.backend.controller;

import com.annysweb.backend.entity.Categorias;
import com.annysweb.backend.service.CategoriasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("categorias")
public class CategoriasController {
    @Autowired
    private final CategoriasService categoriasService;
    public CategoriasController(CategoriasService categoriasService) {
        this.categoriasService = categoriasService;
    }
    @GetMapping("/todas_las_categorias")
    public List<Categorias> getTodasLasCategorias() {
        return categoriasService.getCategorias();
    }
    @GetMapping("/{id_categorias}")
    public Optional<Categorias> getById(@PathVariable("id_categorias") Integer id) {
        return categoriasService.getCategoria(id);
    }
    @PostMapping
    public void saveUpdate(@RequestBody Categorias categorias){
        categoriasService.saveOrUpdate(categorias);
    }
    @DeleteMapping("/{id_categorias}")
    public void saveDelete(@PathVariable("id_categorias") Integer id){
        categoriasService.delete(id);
    }
}
