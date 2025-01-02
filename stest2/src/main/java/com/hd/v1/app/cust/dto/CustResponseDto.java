package com.hd.v1.app.cust.dto;

import com.hd.v1.common.entity.CustEntity;
import com.hd.v1.common.entity.ItemEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@NoArgsConstructor
public class CustResponseDto {
    String id;
    String pwd;
    String name;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public CustResponseDto(CustEntity custEntity) {
        this.id = custEntity.getId();
        this.pwd = custEntity.getPwd();
        this.name = custEntity.getName();
        this.createdAt = custEntity.getCreatedAt();
        this.updatedAt = custEntity.getUpdatedAt();
    }
}
