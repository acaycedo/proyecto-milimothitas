package com.milimothitas.proyecto.proyecto_milimothitas.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para los ítems de una venta")
public class SaleItemRequest {
    @Schema(description = "ID del producto", example = "1")
    private Long productId;

    @Schema(description = "Cantidad del producto", example = "2")
    private Integer quantity;
} 