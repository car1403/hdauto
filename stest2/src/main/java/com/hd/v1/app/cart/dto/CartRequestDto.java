package com.hd.v1.app.cart.dto;


import com.hd.v1.common.entity.CartEntity;
import com.hd.v1.common.entity.CustEntity;
import com.hd.v1.common.entity.ItemEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class CartRequestDto {
    private Long id;
    private Long cnt;
    @NotEmpty(message = "{validation.cart.cust}")
    private String custId;
    private Long itemId;

    public CartEntity toEntity() {
        return CartEntity.builder()
                .id(this.id)
                .cnt(this.cnt)
                .custEntity(CustEntity.builder().id(this.custId).build())
                .itemEntity(ItemEntity.builder().id(this.itemId).build())
                .build();
    }

}
