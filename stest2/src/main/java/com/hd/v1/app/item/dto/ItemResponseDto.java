package com.hd.v1.app.item.dto;

import com.hd.v1.common.entity.ItemEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@NoArgsConstructor
public class ItemResponseDto {
    Long id;
    String name;
    Long price;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public ItemResponseDto(ItemEntity itemEntity) {
        this.id = itemEntity.getId();
        this.name = itemEntity.getName();
        this.price = itemEntity.getPrice();
        this.createdAt = itemEntity.getCreatedAt();
        this.updatedAt = itemEntity.getUpdatedAt();
    }
}
