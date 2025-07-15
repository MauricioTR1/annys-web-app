package com.annysweb.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="proveedores")
@NoArgsConstructor
@AllArgsConstructor
public class Proveedores {
    @Id
    @NotBlank(message="El RFC no debe quedar vacío")
    @Size(max=15, message="El RFC del proveedor no debe pasar de 15 caracteres")
    @Column(name="RFC_proveedor", unique=true, nullable=false)
    private String RFC_proveedor;

    @NotBlank(message="El nombre del proveedor no debe quedar vacío")
    @Size(max=50, message="El nombre del proveedor no debe pasar de 50 caracteres")
    @Column(name="nombre_proveedor", nullable=false)
    private String nombre_proveedor;
}
