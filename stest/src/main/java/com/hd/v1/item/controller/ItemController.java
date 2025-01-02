package com.hd.v1.item.controller;

import com.hd.common.dto.Response;
import com.hd.common.util.Helper;
import com.hd.v1.item.dto.ItemRequestDto;
import com.hd.v1.item.dto.ItemResponseDto;
import com.hd.v1.item.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name="Item CRUD", description = "Item 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/item")
public class ItemController {
    private final ItemService itemService;
    private final Response response;

    @Operation(summary = "상품등록", description = "상품의 이름과 금액 입력")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "성공",
                    content = @Content(schema = @Schema(implementation = ItemResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "name, price 이상",
                    content = @Content(schema = @Schema(implementation = Response.Body.class))),
            @ApiResponse(responseCode = "500", description = "name 중복",
                    content = @Content(schema = @Schema(implementation = Response.Body.class))),
    })
    @PostMapping("/add")
    public ResponseEntity<?> add(@Validated  @RequestBody ItemRequestDto requestDto,
                                 Errors errors) {
        if(errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return response.successCreate(
                new ItemResponseDto(itemService.save(requestDto.toEntity())));
    }
    @GetMapping("/get")
    public ResponseEntity<?> get() {
        return response.success(itemService.getall().stream().map(ItemResponseDto::new).toList());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> get( @Parameter(description = "id", required = true) @PathVariable("id") Long id)  {
        return response.success(new ItemResponseDto(itemService.get(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        return response.success(itemService.remove(id));
    }

    @PatchMapping("/update")
    public ResponseEntity<?> update(@Validated @RequestBody ItemRequestDto dto, Errors errors) {
        // validation check
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return response.success(new ItemResponseDto(itemService.modify(dto.toEntity())));
    }

}
