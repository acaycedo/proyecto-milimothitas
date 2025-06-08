package com.milimothitas.proyecto.proyecto_milimothitas.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.milimothitas.proyecto.proyecto_milimothitas.exceptions.ProductNotFoundException;
import com.milimothitas.proyecto.proyecto_milimothitas.exceptions.ProductInUseException;
import com.milimothitas.proyecto.proyecto_milimothitas.model.dto.ProductRequest;
import com.milimothitas.proyecto.proyecto_milimothitas.model.dto.ProductResponse;
import com.milimothitas.proyecto.proyecto_milimothitas.model.entities.Product;
import com.milimothitas.proyecto.proyecto_milimothitas.repositories.ProductRepository;
import com.milimothitas.proyecto.proyecto_milimothitas.repositories.SaleItemRepository;
import com.milimothitas.proyecto.proyecto_milimothitas.services.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final SaleItemRepository saleItemRepository;

    @Override
    public List<ProductResponse> getAll() {
        return productRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ProductResponse getById(Long id) {
        return productRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ProductNotFoundException("No se ha encontrado el producto"));
    }

    @Override
    public List<ProductResponse> getByCode(String code) {
        return productRepository.findByCodeIgnoringCaseContains(code).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ProductResponse create(ProductRequest product) {
        var entity = toEntity(product);
        var newProduct = productRepository.save(entity);
        return toResponse(newProduct);
    }

    @Override
    public ProductResponse update(Long id, ProductRequest product) {
        //Se usa un Optional para verificar si el producto que se encontro por id existe y si no retornamos una exception de
        //si no esta presente el objeto retorne el mensaje
        var entityOptional = productRepository.findById(id);
        if (!entityOptional.isPresent()) {
            throw new ProductNotFoundException("No se encontró el producto");
        }
        var entity = toEntity(product);
        entity.setId(entityOptional.get().getId());
        var updatedEntity = productRepository.save(entity);

        return toResponse(updatedEntity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("No se ha encontrado el producto"));
        productRepository.delete(product);
    }
    //metodo refactorizado para no tener que repetir esta misma linea en cada metodo y asi ahorrarnos
    //el tema de estar seteando los objetos del Response
    private ProductResponse toResponse(Product product) {
        var response = new ProductResponse();
        response.setId(product.getId());
        response.setCode(product.getCode());
        response.setDescription(product.getDescription());
        response.setCategory(product.getCategory());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setState(product.getState());
        return response;
    }
    //metodo refactorizado para no tener que repetir esta misma linea en cada metodo y asi ahorrarnos
    //el tema de estar seteando los objetos del Request
    private Product toEntity(ProductRequest product) {
        var entity = new Product();
        entity.setCode(product.getCode());
        entity.setDescription(product.getDescription());
        entity.setCategory(product.getCategory());
        entity.setPrice(product.getPrice());
        entity.setStock(product.getStock());
        entity.setState(product.isState());
        return entity;
    }
}
