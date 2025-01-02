package com.hd.v1.item.dto;

import com.hd.v1.item.entity.ItemEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ItemRequestDto {
    Long id;
    @NotEmpty(message = "{validation.item.name}")
    @Schema(description = "Item Name",
            example = "pants1",
            required = true,
            type = "String"
    )
    String name;
    @Min(value=10, message = "{validation.item.price}")
    @Schema(description = "Item Price",
            example = "10000",
            required = true,
            type = "int"
    )
    Long price;

    public ItemEntity toEntity() {
        return ItemEntity.builder()
                .id(this.id)
                .name(this.name)
                .price(this.price)
                .build();
    }

}
