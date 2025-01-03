package com.hd.v1unit.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hd.common.exception.ErrorCode;
import com.hd.common.exception.NameDuplicateException;
import com.hd.v1.app.cart.controller.CartController;
import com.hd.v1.app.cart.dto.CartRequestDto;
import com.hd.v1.app.cart.service.CartService;
import com.hd.v1.app.item.controller.ItemController;
import com.hd.v1.app.item.dto.ItemRequestDto;
import com.hd.v1.app.item.service.ItemService;
import com.hd.v1.common.entity.CartEntity;
import com.hd.v1.common.entity.CustEntity;
import com.hd.v1.common.entity.ItemEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(controllers = CartController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Cart Controller Save Test")
@Import(value = com.hd.common.dto.Response.class)
public class ControllerSaveTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    CartService cartService;

    private ObjectMapper objectMapper;


    @BeforeEach
    public void setup(){
        objectMapper = new ObjectMapper();

    }

    @Test
    @Order(1)
    @DisplayName("Cart Save Test")
    public void test1() throws Exception {
        // given
        CartRequestDto requestDto =
                CartRequestDto.builder().cnt(2L)
                        .custId("id01")
                        .itemId(1L)
                        .build();
        String body = objectMapper.writeValueAsString(requestDto);
        // stub        when(itemRepository.save(any())).thenReturn(itemEntity);
        given(cartService.save(any())).willReturn(
                CartEntity.builder().id(1L).cnt(2L)
                        .custEntity(CustEntity.builder().id("id01").build())
                        .itemEntity(ItemEntity.builder().id(1L).build())
                        .build()
        );
        // when
        ResultActions resultActions =
                mockMvc.perform(post("/api/cart/add")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(body));
        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data.id").value(1L));
        resultActions.andExpect(jsonPath("$.data.cnt").value(2L));
        resultActions.andDo(print());
    }
//
//    // Validated Name
    @Test
    @Order(2)
    @DisplayName("Validated Name Test")
    public void test2() throws Exception {
        CartRequestDto requestDto =
                CartRequestDto.builder().cnt(2L)
                        .custId(null)
                        .itemId(1L)
                        .build();
        String body = objectMapper.writeValueAsString(requestDto);
        // stub        when(itemRepository.save(any())).thenReturn(itemEntity);

        // when
        ResultActions resultActions =
                mockMvc.perform(post("/api/cart/add")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(body));
        // then
        resultActions.andDo(print());
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value(400))
                .andExpect(jsonPath("$.error.[0].field").value("custId"))
                .andExpect(jsonPath("$.error.[0].message").value("Cust Info cannot be empty"));
    }

}
