package com.milimothitas.proyecto.proyecto_milimothitas.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Respuesta con la información de los productos")
public class ProductResponse {
    @Schema(description = "Identificador único del producto", example = "1", required = true)
    private Long id;

    @Schema(description = "Codigo del producto", example = "Termo", required = true)
    private String code;

    @Schema(description = "Descripción del producto", example = "Termo Stanley", required = true)
    private String description;

    @Schema(description = "Categoría del producto", example = "Termos y vasos", required = true)
    private String category;

    @Schema(description = "Precio unitario del producto", example = "12000", required = true, minimum = "0")
    private Double price;

    @Schema(description = "Cantidad disponible en inventario", example = "50", required = true, minimum = "0")
    private Integer stock;

    @Schema(description = "Estado del producto (activo/inactivo)", example = "true", required = false)
    private boolean state;
} 