package com.project.nelson.msvc_carrito.carrito.repository;

import com.project.nelson.msvc_carrito.carrito.model.entity.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemCarritoRepository
  extends JpaRepository<ItemCarrito, Long> {}
