package com.hd.v1.app.cust.dto;


import com.hd.v1.common.entity.CustEntity;
import com.hd.v1.common.entity.ItemEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CustRequestDto {
    @NotEmpty(message = "{validation.cust.id}")
    @Schema(description = "Input Cust ID",
            example = "id01",
            required = true,
            type = "String"
    )
    private String id;
    @NotEmpty(message = "{validation.cust.pwd}")
    @Schema(description = "Input Cust PWD",
            example = "xxxx",
            required = true,
            type = "String"
    )
    private String pwd;
    @NotEmpty(message = "{validation.cust.pwd}")
    @Schema(description = "Input Cust Name",
            example = "name01",
            required = true,
            type = "String"
    )
    private String name;

    public CustEntity toEntity() {
        return CustEntity.builder()
                .id(this.id)
                .pwd(this.pwd)
                .name(this.name)
                .build();
    }

}
