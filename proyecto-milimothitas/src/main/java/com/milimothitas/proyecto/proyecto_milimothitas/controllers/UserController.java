package com.milimothitas.proyecto.proyecto_milimothitas.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.milimothitas.proyecto.proyecto_milimothitas.services.UserService;
import com.milimothitas.proyecto.proyecto_milimothitas.model.dto.UserRequest;
import com.milimothitas.proyecto.proyecto_milimothitas.model.dto.UserResponse;

//RequiredArgsConstructor es una anotacion de lombok que genera un constructor con todos los argumentos finales
@RequiredArgsConstructor
//RestController es una anotacion de spring que indica que la clase es un controlador rest
@RestController
//RequestMapping es una anotacion de spring que indica el path de la url

@RequestMapping("api/users")


public class UserController {
    //UserService es una interfaz que define los servicios de usuario
    //Se establece final ya que es constante y es el llamado a dicho servicio
    private final UserService userService;

    //Metodo para obtener todos los usuarios
    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAll();
    }
    @GetMapping("{id}")
    public UserResponse getUserById(@PathVariable("id") Long id){
        return userService.getById(id);
    }

    @GetMapping("search")
    public List<UserResponse> getUsersByName(@RequestParam("nombre") String name){
        return userService.getByName(name);
    }
    @PostMapping
    public UserResponse createUser(@RequestBody UserRequest user){
        return userService.create(user);
    }
    @PutMapping("{id}")
    public UserResponse updateUser(@PathVariable("id") Long id, @RequestBody UserRequest user){
        return userService.update(id,user);
    }
    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable("id") Long id){
       userService.delete(id);
    }

}
