package com.milimothitas.proyecto.proyecto_milimothitas.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException (String message){
        super(message);
    }
}
