package com.milimothitas.proyecto.proyecto_milimothitas.model.dto;
//clase usada para enviar los datos de un usuario

import lombok.Data;
@Data
public class UserResponse {
    private Long userId;
    private String name;
    private String email;
    private String password;
    private String role;
    private String state;
    
}
