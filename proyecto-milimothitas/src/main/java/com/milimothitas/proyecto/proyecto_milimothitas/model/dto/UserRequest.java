package com.milimothitas.proyecto.proyecto_milimothitas.model.dto;

import lombok.Data;

@Data
//Clase usada para recibir los datos de un usuario
public class UserRequest {
    private String name;
    private String email;
    private String password;
    private String role;
    private String state;
}
