package com.milimothitas.proyecto.proyecto_milimothitas.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para la respuesta de ventas")
public class SaleResponse {
    @Schema(description = "ID único de la venta", example = "1")
    private Long id;

    @Schema(description = "Fecha y hora de la venta", example = "2024-03-20T15:30:00")
    private LocalDateTime fecha;

    @Schema(description = "Subtotal de la venta sin IVA", example = "10000.0")
    private Double subtotal;

    @Schema(description = "Valor del IVA (19%)", example = "1900.0")
    private Double iva;

    @Schema(description = "Total de la venta con IVA y descuento", example = "11900.0")
    private Double totalConIva;

    @Schema(description = "Descuento aplicado a la venta", example = "1000.0")
    private Double descuento;

    @Schema(description = "Lista de ítems de productos vendidos")
    private List<SaleItemResponse> saleItems;
} 