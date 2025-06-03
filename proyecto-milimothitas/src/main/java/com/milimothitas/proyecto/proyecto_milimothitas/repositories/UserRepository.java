package com.milimothitas.proyecto.proyecto_milimothitas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.milimothitas.proyecto.proyecto_milimothitas.model.entities.User;

//principio SOLID: Single Responsibility Principle donde se crea una interfaz para cada entidad curse
public interface UserRepository extends JpaRepository<User, Long> {
    //Otra cosa que nos permite JPA es hacer consultas avanzadas es decir:

    List<User> findByNameIgnoringCaseContains(String name);

}
