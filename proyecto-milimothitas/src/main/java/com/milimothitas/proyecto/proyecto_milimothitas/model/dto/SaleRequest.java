package com.milimothitas.proyecto.proyecto_milimothitas.model.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para la creación y actualización de ventas")
public class SaleRequest {
    @Schema(description = "Lista de ítems de productos a incluir en la venta")
    private List<SaleItemRequest> saleItems;

    @Schema(description = "Descuento a aplicar a la venta", example = "1000.0")
    private Double descuento;
} 