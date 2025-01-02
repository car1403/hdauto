package com.hd.v1.app.cart.repository;

import com.hd.v1.common.entity.CartEntity;
import com.hd.v1.common.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity,Long> {
    List<CartEntity> findByCustId(String id);
}