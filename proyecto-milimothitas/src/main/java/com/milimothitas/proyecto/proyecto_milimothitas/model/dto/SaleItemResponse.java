package com.milimothitas.proyecto.proyecto_milimothitas.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para la respuesta de ítems de venta")
public class SaleItemResponse {
    @Schema(description = "ID único del ítem de venta", example = "1")
    private Long id;

    @Schema(description = "Producto vendido")
    private ProductResponse product;

    @Schema(description = "Cantidad del producto", example = "2")
    private Integer quantity;

    @Schema(description = "Precio del producto al momento de la venta", example = "35000.0")
    private Double priceAtSale;
} 