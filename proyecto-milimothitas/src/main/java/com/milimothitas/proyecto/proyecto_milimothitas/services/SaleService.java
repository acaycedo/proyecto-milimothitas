package com.milimothitas.proyecto.proyecto_milimothitas.services;

import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

import com.milimothitas.proyecto.proyecto_milimothitas.model.dto.SaleRequest;
import com.milimothitas.proyecto.proyecto_milimothitas.model.dto.SaleResponse;

public interface SaleService {
    List<SaleResponse> getAll();
    SaleResponse getById(Long id);
    SaleResponse create(SaleRequest sale);
    SaleResponse aplicarDescuento(Long saleId, Double descuento);
    SaleResponse calcularSubtotal(Long saleId);
    SaleResponse calcularIva(Long saleId);
    SaleResponse calcularTotal(Long saleId);
    SaleResponse detalleDeVenta(Long saleId);
    Map<String, Double> getSalesSummaryByMonth();
    List<SaleResponse> getSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate);
} 