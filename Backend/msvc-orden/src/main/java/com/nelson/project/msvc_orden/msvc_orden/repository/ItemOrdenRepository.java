package com.nelson.project.msvc_orden.msvc_orden.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nelson.project.msvc_orden.msvc_orden.model.entity.OrdenItem;

@Repository
public interface ItemOrdenRepository
  extends JpaRepository<OrdenItem, Long> {}