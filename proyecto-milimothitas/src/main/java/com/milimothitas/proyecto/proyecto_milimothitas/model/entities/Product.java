package com.milimothitas.proyecto.proyecto_milimothitas.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

//@Data es una anotacion de lombok que genera los getters, setters, toString, equals y hashCode
@Data
//@Entity es una anotacion de JPA que indica que la clase es una entidad
@Entity
//@Table es una anotacion de JPA que indica el nombre de la tabla en la base de datos
@Table(name = "products")
public class Product {
    //Todo esto es para que la tabla se llame products
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "categoria", nullable = false, unique = true)
    private String category;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "state", nullable = false)
    private Boolean state;

}