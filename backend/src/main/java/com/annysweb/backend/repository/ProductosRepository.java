package com.annysweb.backend.repository;

import com.annysweb.backend.entity.Productos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductosRepository extends JpaRepository<Productos, String> {

}
