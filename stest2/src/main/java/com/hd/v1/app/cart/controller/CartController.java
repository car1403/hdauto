package com.hd.v1.app.cart.controller;

import com.hd.common.dto.Response;
import com.hd.common.util.Helper;
import com.hd.v1.app.cart.dto.CartRequestDto;
import com.hd.v1.app.cart.dto.CartResponseDto;
import com.hd.v1.app.cart.service.CartService;
import com.hd.v1.app.cust.dto.CustRequestDto;
import com.hd.v1.app.cust.dto.CustResponseDto;
import com.hd.v1.app.cust.service.CustService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name="Cart CRUD", description = "Cart 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final Response response;

    @Operation(summary = "Cart등록", description = "상품정보, 사용자정보, 수량 입력")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "성공",
                    content = @Content(schema = @Schema(implementation = CartResponseDto.class))),
//            @ApiResponse(responseCode = "400", description = "name, price 이상",
//                    content = @Content(schema = @Schema(implementation = Response.Body.class))),
//            @ApiResponse(responseCode = "500", description = "name 중복",
//                    content = @Content(schema = @Schema(implementation = Response.Body.class))),
    })
    @PostMapping("/add")
    public ResponseEntity<?> add(@Validated  @RequestBody CartRequestDto requestDto,
                                 Errors errors) {
        if(errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return response.successCreate(
                new CartResponseDto(cartService.save(requestDto.toEntity())));
    }
    @GetMapping("/get")
    public ResponseEntity<?> get() {
        return response.success(cartService.getall().stream().map(CartResponseDto::new).toList());
    }
    @GetMapping("/get/cust/{id}")
    public ResponseEntity<?> getcust( @Parameter(description = "id", required = true) @PathVariable("id") String id)  {
        return response.success(cartService.getall(id).stream().map(CartResponseDto::new).toList());
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<?> get( @Parameter(description = "id", required = true) @PathVariable("id") Long id)  {
        return response.success(new CartResponseDto(cartService.get(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        return response.success(cartService.remove(id));
    }

    @PatchMapping("/update")
    public ResponseEntity<?> update(@Validated @RequestBody CartRequestDto dto, Errors errors) {
        // validation check
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return response.success(new CartResponseDto(cartService.modify(dto.toEntity())));
    }

}
