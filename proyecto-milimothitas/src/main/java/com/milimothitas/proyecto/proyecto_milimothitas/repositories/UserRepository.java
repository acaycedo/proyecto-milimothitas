package com.milimothitas.proyecto.proyecto_milimothitas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.milimothitas.proyecto.proyecto_milimothitas.model.entities.User;

//principio SOLID: Single Responsibility Principle donde se crea una interfaz para cada entidad curse
public interface UserRepository extends JpaRepository<User, Long> {

}
