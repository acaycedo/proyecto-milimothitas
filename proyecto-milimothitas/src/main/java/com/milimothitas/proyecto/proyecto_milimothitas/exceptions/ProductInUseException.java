package com.milimothitas.proyecto.proyecto_milimothitas.exceptions;

public class ProductInUseException extends RuntimeException {
    public ProductInUseException(String message) {
        super(message);
    }
} 