package com.milimothitas.proyecto.proyecto_milimothitas.services.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.milimothitas.proyecto.proyecto_milimothitas.exceptions.InsufficientStockException;
import com.milimothitas.proyecto.proyecto_milimothitas.exceptions.ProductNotFoundException;
import com.milimothitas.proyecto.proyecto_milimothitas.exceptions.SaleNotFoundException;
import com.milimothitas.proyecto.proyecto_milimothitas.model.dto.ProductResponse;
import com.milimothitas.proyecto.proyecto_milimothitas.model.dto.SaleItemResponse;
import com.milimothitas.proyecto.proyecto_milimothitas.model.dto.SaleRequest;
import com.milimothitas.proyecto.proyecto_milimothitas.model.dto.SaleResponse;
import com.milimothitas.proyecto.proyecto_milimothitas.model.entities.Product;
import com.milimothitas.proyecto.proyecto_milimothitas.model.entities.Sale;
import com.milimothitas.proyecto.proyecto_milimothitas.model.entities.SaleItem;
import com.milimothitas.proyecto.proyecto_milimothitas.repositories.ProductRepository;
import com.milimothitas.proyecto.proyecto_milimothitas.repositories.SaleItemRepository;
import com.milimothitas.proyecto.proyecto_milimothitas.repositories.SaleRepository;
import com.milimothitas.proyecto.proyecto_milimothitas.services.SaleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final SaleItemRepository saleItemRepository;
    private static final double IVA_RATE = 0.19; // 19% IVA

    @Override
    public List<SaleResponse> getAll() {
        return saleRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public SaleResponse getById(Long id) {
        return saleRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new SaleNotFoundException("No se ha encontrado la venta"));
    }

    @Override
    @Transactional
    public SaleResponse create(SaleRequest request) {
        Sale sale = new Sale();
        sale.setFecha(LocalDateTime.now());
        sale.setSubtotal(0.0);
        sale.setIva(0.0);
        sale.setTotalConIva(0.0);
        sale.setDescuento(request.getDescuento() != null ? request.getDescuento() : 0.0);
        sale.setSaleItems(new ArrayList<>());

        if (request.getSaleItems() != null) {
            for (var itemRequest : request.getSaleItems()) {
                Product product = productRepository.findById(itemRequest.getProductId())
                        .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado: " + itemRequest.getProductId()));
                
                // Validar stock
                if (product.getStock() < itemRequest.getQuantity()) {
                    throw new InsufficientStockException("Stock insuficiente para el producto: " + product.getDescription() + ". Stock disponible: " + product.getStock());
                }

                SaleItem saleItem = new SaleItem();
                saleItem.setSale(sale);
                saleItem.setProduct(product);
                saleItem.setQuantity(itemRequest.getQuantity());
                saleItem.setPriceAtSale(product.getPrice()); // Captura el precio actual del producto
                sale.getSaleItems().add(saleItem);

                // Actualizar stock del producto
                product.setStock(product.getStock() - itemRequest.getQuantity());
                productRepository.save(product); // Guardar el producto con el stock actualizado
            }
        }
        
        Sale savedSale = saleRepository.save(sale);
        saleItemRepository.saveAll(savedSale.getSaleItems()); // Guardar los SaleItems después de la venta
        return calcularTotal(savedSale.getId());
    }

    @Override
    @Transactional
    public SaleResponse aplicarDescuento(Long saleId, Double descuento) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException("No se ha encontrado la venta"));
        
        sale.setDescuento(descuento);
        Sale savedSale = saleRepository.save(sale);
        return calcularTotal(savedSale.getId());
    }

    @Override
    @Transactional
    public SaleResponse calcularSubtotal(Long saleId) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException("No se ha encontrado la venta"));
        
        double subtotal = sale.getSaleItems().stream()
                .mapToDouble(item -> item.getPriceAtSale() * item.getQuantity())
                .sum();
        
        sale.setSubtotal(subtotal);
        Sale savedSale = saleRepository.save(sale);
        return toResponse(savedSale);
    }

    @Override
    @Transactional
    public SaleResponse calcularIva(Long saleId) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException("No se ha encontrado la venta"));
        
        double iva = sale.getSubtotal() * IVA_RATE;
        sale.setIva(iva);
        Sale savedSale = saleRepository.save(sale);
        return toResponse(savedSale);
    }

    @Override
    @Transactional
    public SaleResponse calcularTotal(Long saleId) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException("No se ha encontrado la venta"));
        
        calcularSubtotal(saleId);
        calcularIva(saleId);
        
        double total = sale.getSubtotal() + sale.getIva() - sale.getDescuento();
        sale.setTotalConIva(total);
        
        Sale savedSale = saleRepository.save(sale);
        return toResponse(savedSale);
    }

    @Override
    public SaleResponse detalleDeVenta(Long saleId) {
        return getById(saleId);
    }

    @Override
    public Map<String, Double> getSalesSummaryByMonth() {
        return saleRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                    sale -> sale.getFecha().format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                    Collectors.summingDouble(Sale::getTotalConIva)
                ));
    }

    @Override
    public List<SaleResponse> getSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.findByFechaBetween(startDate, endDate).stream()
                .map(this::toResponse)
                .toList();
    }

    private SaleResponse toResponse(Sale sale) {
        SaleResponse response = new SaleResponse();
        response.setId(sale.getId());
        response.setFecha(sale.getFecha());
        response.setSubtotal(sale.getSubtotal());
        response.setIva(sale.getIva());
        response.setTotalConIva(sale.getTotalConIva());
        response.setDescuento(sale.getDescuento());
        response.setSaleItems(sale.getSaleItems().stream()
                .map(this::toSaleItemResponse)
                .toList());
        return response;
    }

    private SaleItemResponse toSaleItemResponse(SaleItem saleItem) {
        SaleItemResponse response = new SaleItemResponse();
        response.setId(saleItem.getId());
        response.setProduct(toProductResponse(saleItem.getProduct()));
        response.setQuantity(saleItem.getQuantity());
        response.setPriceAtSale(saleItem.getPriceAtSale());
        return response;
    }

    private ProductResponse toProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setCode(product.getCode());
        response.setDescription(product.getDescription());
        response.setCategory(product.getCategory());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setState(product.getState());
        return response;
    }
} 