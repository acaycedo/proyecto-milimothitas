package com.milimothitas.proyecto.proyecto_milimothitas.services;

import java.util.List;

import com.milimothitas.proyecto.proyecto_milimothitas.model.dto.ProductoRequest;
import com.milimothitas.proyecto.proyecto_milimothitas.model.dto.ProductResponse;

public interface ProductService {
    List<ProductResponse> getAll();
    ProductResponse getById(Long id);
    List<ProductResponse> getByName(String name);
    ProductResponse create(ProductoRequest product);
    ProductResponse update(Long id, ProductoRequest product);
    void delete(Long id);
} 