package com.milimothitas.proyecto.proyecto_milimothitas.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Solicitud de la información de los productos")
public class ProductoRequest {
    @NotBlank(message = "El campo Nombre es obligatiorio")
    @Schema(description = "Nombre del producto", example = "Termo", required = true)
    private String name;

    @Schema(description = "Descripción del producto", example = "Termo Stanley", required = true)
    private String description;
    @NotBlank(message = "El campo Nombre es obligatiorio")
    @Schema(description = "Categoría del producto", example = "Termos y vasos", required = true)
    private String category;
    @NotBlank(message = "El campo Nombre es obligatiorio")
    @Schema(description = "Precio unitario del producto", example = "$12.000", required = true, minimum = "0")
    private Double price;
    @NotBlank(message = "El campo Nombre es obligatiorio")
    @Schema(description = "Cantidad disponible en inventario", example = "50", required = true, minimum = "0")
    private Integer stock;

    @Schema(description = "Estado del producto (activo/inactivo)", example = "true", required = true)
    private boolean state;
}
