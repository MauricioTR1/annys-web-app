package com.annysweb.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Table(name="productos")
@NoArgsConstructor
@AllArgsConstructor
public class Productos {
    @Id
    @NotBlank(message="El código del producto es obligatorio")
    @Size(max=10, message="El código de producto no puede pasar de 10 caracteres")
    @Column(name="codigo_producto", nullable = false, unique = true, length = 10)
    private String codigo_producto;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max=80, message = "El nombre del producto no puede pasar de 80 caracteres")
    @Column(name="nombre_producto", nullable = false, length = 80)
    private String nombre_producto;

    @NotBlank(message="La descripción del producto es obligatoria")
    @Size(max=100, message="La descripción del producto no puede pasar de 100 caracteres")
    @Column(name="descripcion_producto", nullable = true, length = 100) // puede o no tener descripción
    private String descripcion_producto;

    @NotNull(message="El precio del producto es obligatorio")
    @Min(value=0, message="El precio del producto no debe ser negativo")
    @Column(name="precio_producto", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio_producto;

    @NotNull(message = "El stock del producto es obligatorio")
    @Min(value = 0, message = "El stock del producto no puede ser negativo")
    @Column(name = "stock_producto", nullable = false)
    private int stock_producto;

    @ManyToOne
    @JoinColumn(name="id_categoria", nullable=false)
    @NotNull(message="La categoría del producto es obligatoria")
    private Categorias categoria;
}
