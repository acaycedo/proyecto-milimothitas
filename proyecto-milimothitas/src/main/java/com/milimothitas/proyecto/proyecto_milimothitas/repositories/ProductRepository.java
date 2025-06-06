package com.milimothitas.proyecto.proyecto_milimothitas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.milimothitas.proyecto.proyecto_milimothitas.model.entities.Product;

//principio SOLID: Single Responsibility Principle donde se crea una interfaz para cada entidad product
public interface ProductRepository extends JpaRepository<Product, Long> {
    //control de busqueda por nombre de productos para no tener que hacer Querys complejas con mucho codigo
    //Lo malo que Java tiene limitacion de caracteres por metodo asi que si es necesario si hay que implememtar query
    List<Product> findByNameIgnoringCaseContains(String name);
}
