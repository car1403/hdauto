package com.hd.v1.app.cart.dto;

import com.hd.v1.common.entity.CartEntity;
import com.hd.v1.common.entity.CustEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CartResponseDto {
    private Long id;
    private Long cnt;
    private String custId;
    private Long itemId;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public CartResponseDto(CartEntity cartEntity) {
        this.id = cartEntity.getId();
        this.cnt = cartEntity.getCnt();
        this.custId = cartEntity.getCust().getId();
        this.itemId = cartEntity.getItem().getId();
        this.createdAt = cartEntity.getCreatedAt();
        this.updatedAt = cartEntity.getUpdatedAt();
    }
}
