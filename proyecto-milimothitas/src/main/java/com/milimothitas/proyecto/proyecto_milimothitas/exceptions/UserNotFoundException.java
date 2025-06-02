package com.milimothitas.proyecto.proyecto_milimothitas.exceptions;

public class UserNotFoundException extends RuntimeException {
    //Excepcion creada solo para recibir el mensaje heredado de Runtime y pasarlo cuando hacemos consultas por id
    public UserNotFoundException (String message){
        super(message);
    }
    
}
