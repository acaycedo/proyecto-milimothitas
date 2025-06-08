package com.milimothitas.proyecto.proyecto_milimothitas.services;

import java.util.List;

import com.milimothitas.proyecto.proyecto_milimothitas.model.dto.ProductRequest;
import com.milimothitas.proyecto.proyecto_milimothitas.model.dto.ProductResponse;

public interface ProductService {
    List<ProductResponse> getAll();
    ProductResponse getById(Long id);
    List<ProductResponse> getByCode(String code);
    ProductResponse create(ProductRequest product);
    ProductResponse update(Long id, ProductRequest product);
    void delete(Long id);
} 