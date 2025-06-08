package com.milimothitas.proyecto.proyecto_milimothitas.controllers;

import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.milimothitas.proyecto.proyecto_milimothitas.exceptions.InsufficientStockException;
import com.milimothitas.proyecto.proyecto_milimothitas.exceptions.SaleNotFoundException;
import com.milimothitas.proyecto.proyecto_milimothitas.model.dto.SaleRequest;
import com.milimothitas.proyecto.proyecto_milimothitas.model.dto.SaleResponse;
import com.milimothitas.proyecto.proyecto_milimothitas.services.SaleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
@Tag(name = "Ventas", description = "API para la gestión de ventas")
public class SaleController {

    private final SaleService saleService;

    @GetMapping
    @Operation(summary = "Obtener todas las ventas")
    @ApiResponse(responseCode = "200", description = "Lista de ventas obtenida exitosamente")
    public ResponseEntity<List<SaleResponse>> getAll() {
        return ResponseEntity.ok(saleService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una venta por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Venta encontrada"),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    public ResponseEntity<SaleResponse> getById(
            @Parameter(description = "ID de la venta") @PathVariable Long id) {
        return ResponseEntity.ok(saleService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva venta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Venta creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Cantidad de stock insuficiente o datos inválidos")
    })
    public ResponseEntity<SaleResponse> create(
            @Parameter(description = "Datos de la venta") @RequestBody SaleRequest request) {
        return ResponseEntity.ok(saleService.create(request));
    }

    @PutMapping("/{saleId}/discount")
    @Operation(summary = "Aplicar descuento a una venta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Descuento aplicado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    public ResponseEntity<SaleResponse> aplicarDescuento(
            @Parameter(description = "ID de la venta") @PathVariable Long saleId,
            @Parameter(description = "Valor del descuento") @RequestParam Double descuento) {
        return ResponseEntity.ok(saleService.aplicarDescuento(saleId, descuento));
    }

    @PostMapping("/{saleId}/calculate/subtotal")
    @Operation(summary = "Calcular subtotal de una venta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subtotal calculado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    public ResponseEntity<SaleResponse> calcularSubtotal(
            @Parameter(description = "ID de la venta") @PathVariable Long saleId) {
        return ResponseEntity.ok(saleService.calcularSubtotal(saleId));
    }

    @PostMapping("/{saleId}/calculate/iva")
    @Operation(summary = "Calcular IVA de una venta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "IVA calculado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    public ResponseEntity<SaleResponse> calcularIva(
            @Parameter(description = "ID de la venta") @PathVariable Long saleId) {
        return ResponseEntity.ok(saleService.calcularIva(saleId));
    }

    @PostMapping("/{saleId}/calculate/total")
    @Operation(summary = "Calcular total de una venta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Total calculado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    public ResponseEntity<SaleResponse> calcularTotal(
            @Parameter(description = "ID de la venta") @PathVariable Long saleId) {
        return ResponseEntity.ok(saleService.calcularTotal(saleId));
    }

    @GetMapping("/{saleId}/details")
    @Operation(summary = "Obtener detalles de una venta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Detalles obtenidos exitosamente"),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    public ResponseEntity<SaleResponse> detalleDeVenta(
            @Parameter(description = "ID de la venta") @PathVariable Long saleId) {
        return ResponseEntity.ok(saleService.detalleDeVenta(saleId));
    }

    @GetMapping("/summary-by-month")
    @Operation(summary = "Obtener resumen de ventas por mes")
    @ApiResponse(responseCode = "200", description = "Resumen de ventas obtenido exitosamente")
    public ResponseEntity<Map<String, Double>> getSalesSummaryByMonth() {
        return ResponseEntity.ok(saleService.getSalesSummaryByMonth());
    }

    @GetMapping("/filter-by-date")
    @Operation(summary = "Obtener ventas por rango de fechas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ventas obtenidas exitosamente"),
        @ApiResponse(responseCode = "400", description = "Fechas inválidas")
    })
    public ResponseEntity<List<SaleResponse>> getSalesByDateRange(
            @Parameter(description = "Fecha de inicio (yyyy-MM-ddTHH:mm:ss)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Fecha de fin (yyyy-MM-ddTHH:mm:ss)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(saleService.getSalesByDateRange(startDate, endDate));
    }

    @ExceptionHandler(InsufficientStockException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleInsufficientStockException(InsufficientStockException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(SaleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleSaleNotFoundException(SaleNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
} 