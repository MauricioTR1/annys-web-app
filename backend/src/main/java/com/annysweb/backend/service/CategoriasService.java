package com.annysweb.backend.service;

import com.annysweb.backend.entity.Categorias;
import com.annysweb.backend.repository.CategoriasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriasService {
    @Autowired
    CategoriasRepository categoriasRepository;
    public List<Categorias> getCategorias() {
        return categoriasRepository.findAll();
    }

    public Optional<Categorias> getCategoria(Integer id_categoria) {
        return categoriasRepository.findById(id_categoria);
    }

    public void saveOrUpdate(Categorias categorias) {
        categoriasRepository.save(categorias);
    }

    public void delete(Integer id_categoria) {
        categoriasRepository.deleteById(id_categoria);
    }
}
