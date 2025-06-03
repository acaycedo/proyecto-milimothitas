package com.milimothitas.proyecto.proyecto_milimothitas.services.impl;

//import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.milimothitas.proyecto.proyecto_milimothitas.exceptions.UserNotFoundException;
import com.milimothitas.proyecto.proyecto_milimothitas.model.dto.UserRequest;
import com.milimothitas.proyecto.proyecto_milimothitas.model.dto.UserResponse;
import com.milimothitas.proyecto.proyecto_milimothitas.model.entities.User;
import com.milimothitas.proyecto.proyecto_milimothitas.repositories.UserRepository;
import com.milimothitas.proyecto.proyecto_milimothitas.services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.var;

//Es mejor tener varias interfaces pequeñas que implementan todo lo necesario que una completa que implementa cosas que no se usan
//Por ende cada interfaz debe tener un proposito, y no debemos forzar a clases a implementar metodos que no necesitan. Y recordemos que tenemos dos roles los cuales uno se rige de no tener las mismas caracteristicas que el otro.
@Service
@RequiredArgsConstructor
// Gracias a Required buscara todo lo que este parametrizado con Final y creara
// el debido contructor con lo necesario.
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // Recordemos que dentro del esquema de arquitectura el servicio se conecta con
    // el repositorio pero esto lo hace por medio de un DTO el DTO se comunica con
    // la entidad para que esta pueda enviar los datos al repositorio.
    // Este con el objetivo de enviar solo lo necesario o lo que se este creando o
    // usando.
    @Override
    // Aca recordemos que el servicio debe retornar al controlador pero el
    // repositorio siempre devuelve un Entitie, por lo tanto el controlador no
    // entiende que son las entidades asi que debemos volver a convertir
    // las entidades en DTO y asi el controlador pueda entender la informacion.
    public List<UserResponse> getAll() {
        /*List<UserResponse> responses = new ArrayList<>();
        var users = userRepository.findAll();
        // El For recorre la lista de entidades que se obtuvieron y extraera cada
        // caracteristica de la entidad para poder tener objetos
        for (var user : users) {
            responses.add(toResponse(user));
        }
        return responses;
        */

        return userRepository.findAll().stream().map(this::toResponse).toList(); //Esta linea es una optimizacion a todo el codigo de arriba

    }

    @Override
    public UserResponse getById(Long id) {
       return userRepository.findById(id)
       .map(this::toResponse).orElseThrow(()-> new UserNotFoundException("No se ha encontrado el usuario"));
    }

    @Override
    public List<UserResponse> getByName(String name) {
        return userRepository.findByNameIgnoringCaseContains(name).stream()
       .map(this::toResponse).toList();
    }

    @Override
    public UserResponse create(UserRequest user) {
        var entity = toEntity(user);
        var newUser = userRepository.save(entity);
        return toResponse(newUser);
    }

    @Override
    public UserResponse update(Long id, UserRequest user) {
        var entityOptional = userRepository.findById(id);
        if (!entityOptional.isPresent()) {
            throw new UserNotFoundException("No se encontró el usuario");
        }
        var entity = toEntity(user);
        entity.setId(entityOptional.get().getId());
        var updatedEntity = userRepository.save(entity);

        return toResponse(updatedEntity);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    // Metodo privado usado solo para traer los campos de la respuesta.
    private UserResponse toResponse(User user) {
        var response = new UserResponse();
        response.setUserId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPassword(user.getPassword());
        response.setRole(user.getRole());

        return response;
    }

    private User toEntity(UserRequest user){
        var entity = new User();
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        entity.setRole(user.getRole());
        return entity;
    }
}
