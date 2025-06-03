package com.milimothitas.proyecto.proyecto_milimothitas.services;

import java.util.List;

import com.milimothitas.proyecto.proyecto_milimothitas.model.dto.UserRequest;
import com.milimothitas.proyecto.proyecto_milimothitas.model.dto.UserResponse;

//Interface para el servicio de usuario
//Todas las funciones que se van a usar en el controlador esto es determinado por los diagramas de secuencia
public interface UserService {
    //metodo para obtener todos los usuarios
    List<UserResponse> getAll();
    //metodo para obtener un usuario por su id
    UserResponse getById(Long id);
    //metodo para obtener un usuario por su categoria
    List<UserResponse> getByName(String name); 
   //metodo para crear un usuario
    UserResponse create(UserRequest user);
    //metodo para actualizar un usuario
    UserResponse update(Long id, UserRequest user);
    //metodo para eliminar un usuario
    void delete(Long id);

}