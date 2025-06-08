package com.milimothitas.proyecto.proyecto_milimothitas.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.milimothitas.proyecto.proyecto_milimothitas.services.ProductService;
import com.milimothitas.proyecto.proyecto_milimothitas.exceptions.ProductNotFoundException;
import com.milimothitas.proyecto.proyecto_milimothitas.exceptions.ProductInUseException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import com.milimothitas.proyecto.proyecto_milimothitas.model.dto.ProductRequest;
import com.milimothitas.proyecto.proyecto_milimothitas.model.dto.ProductResponse;

@Tag(name = "Productos", description = "EndPoint para gestionar los productos de Milimothitas")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/products")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "Lista Productos creados", description = "Lista todos los productos creados con sus datos existentes en el sistema. Incluye activos e inactivos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class)))
    })
    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAll();
    }

    @Operation(summary = "Obtener producto por ID", description = "Obtiene un producto específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "ID de producto inválido (ej. menor a 1)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductNotFoundException.class)))
    })
    @GetMapping("{id}")
    public ProductResponse getProductById(
            @Parameter(description = "ID del producto a buscar", required = true, example = "1") @Valid @Min(1) @PathVariable("id") Long id) {
        return productService.getById(id);
    }

    @Operation(summary = "Buscar productos por nombre", description = "Busca productos por una parte de su nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos encontrada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Parámetro 'nombre' vacío o inválido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @GetMapping("search")
    public List<ProductResponse> getProductsByCode(
            @Parameter(description = "Nombre o parte del nombre del producto a buscar", required = true, example = "Termo") @Valid @NotBlank @RequestParam("codigo") String code) {
        return productService.getByCode(code);
    }

    @Operation(summary = "Crear un nuevo producto", description = "Crea un nuevo producto en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de producto inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del producto a crear", required = true, content = @Content(schema = @Schema(implementation = ProductRequest.class))) @Valid @RequestBody ProductRequest product) {
        return productService.create(product);
    }

    @Operation(summary = "Actualizar un producto existente", description = "Actualiza la información de un producto por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "ID de producto o datos de actualización inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductNotFoundException.class)))
    })
    @PutMapping("{id}")
    public ProductResponse updateProduct(
            @Parameter(description = "ID del producto a actualizar", required = true, example = "1") @Valid @Min(1) @PathVariable("id") Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos del producto", required = true, content = @Content(schema = @Schema(implementation = ProductRequest.class))) @RequestBody ProductRequest product) {
        return productService.update(id, product);
    }

    @Operation(summary = "Eliminar un producto", description = "Elimina un producto del sistema por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID de producto inválido (ej. menor a 1)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductNotFoundException.class)))
    })
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(
            @Parameter(description = "ID del producto a eliminar", required = true, example = "1") @Valid @Min(1) @PathVariable("id") Long id) {
        productService.delete(id);
    }
}
