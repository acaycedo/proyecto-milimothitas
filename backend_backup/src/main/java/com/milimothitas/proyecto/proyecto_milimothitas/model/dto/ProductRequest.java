package com.milimothitas.proyecto.proyecto_milimothitas.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Solicitud de la información de los productos")
public class ProductRequest {
    @Schema(description = "Codigo del producto", example = "Nombre Producto", required = true)
    @NotBlank(message = "El campo Nombre es obligatiorio")
    private String code;

    @Schema(description = "Descripción del producto", example = "Descripcion del producto", required = true)
    @NotBlank(message = "El campo Descripcion es obligatiorio")
    private String description;

    @Schema(description = "Categoría del producto", example = "Categoria que pertenece", required = true)
    @NotBlank(message = "El campo Categoria es obligatiorio")
    private String category;


    private Double price;

    private Integer stock;

    @Schema(description = "Estado del producto (activo/inactivo)", example = "true", required = false)
    private boolean state;
}
