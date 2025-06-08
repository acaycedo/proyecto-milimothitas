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
@Table(name = "users")
public class User {
    //Todo esto es para que la tabla se llame users
    @Id
    //Generara un id automaticamente
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role;
    
    
    
}
