package com.milimothitas.proyecto.proyecto_milimothitas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.milimothitas.proyecto.proyecto_milimothitas.model.entities.Sale;
import java.time.LocalDateTime;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findByFechaBetween(LocalDateTime startDate, LocalDateTime endDate);
} 