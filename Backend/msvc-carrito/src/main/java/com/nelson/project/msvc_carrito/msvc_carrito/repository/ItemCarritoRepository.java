package com.nelson.project.msvc_carrito.msvc_carrito.repository;

import com.nelson.project.msvc_carrito.msvc_carrito.model.entity.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemCarritoRepository
  extends JpaRepository<ItemCarrito, Long> {}
