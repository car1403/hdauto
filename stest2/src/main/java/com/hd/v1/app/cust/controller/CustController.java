package com.hd.v1.app.cust.controller;

import com.hd.common.dto.Response;
import com.hd.common.util.Helper;

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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name="Cust CRUD", description = "Cust 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cust")
public class CustController {
    private final CustService custService;
    private final Response response;

    @Operation(summary = "Cust등록", description = "ID, PWD, NAME 입력")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "성공",
                    content = @Content(schema = @Schema(implementation = CustResponseDto.class))),
//            @ApiResponse(responseCode = "400", description = "name, price 이상",
//                    content = @Content(schema = @Schema(implementation = Response.Body.class))),
//            @ApiResponse(responseCode = "500", description = "name 중복",
//                    content = @Content(schema = @Schema(implementation = Response.Body.class))),
    })
    @PostMapping("/add")
    public ResponseEntity<?> add(@Validated  @RequestBody CustRequestDto requestDto,
                                 Errors errors) {
        if(errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return response.successCreate(
                new CustResponseDto(custService.save(requestDto.toEntity())));
    }
    @GetMapping("/get")
    public ResponseEntity<?> get() {
        return response.success(custService.getall().stream().map(CustResponseDto::new).toList());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> get( @Parameter(description = "id", required = true) @PathVariable("id") String id)  {
        return response.success(new CustResponseDto(custService.get(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        return response.success(custService.remove(id));
    }

    @PatchMapping("/update")
    public ResponseEntity<?> update(@Validated @RequestBody CustRequestDto dto, Errors errors) {
        // validation check
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return response.success(new CustResponseDto(custService.modify(dto.toEntity())));
    }

}
