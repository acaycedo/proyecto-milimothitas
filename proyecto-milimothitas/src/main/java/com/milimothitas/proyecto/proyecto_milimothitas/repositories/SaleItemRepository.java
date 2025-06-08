package com.milimothitas.proyecto.proyecto_milimothitas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.milimothitas.proyecto.proyecto_milimothitas.model.entities.SaleItem;

public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {
    boolean existsByProductId(Long productId);
} 