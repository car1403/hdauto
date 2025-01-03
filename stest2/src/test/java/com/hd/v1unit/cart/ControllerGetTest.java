package com.hd.v1unit.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hd.common.exception.ErrorCode;
import com.hd.common.exception.IdNotFoundException;
import com.hd.v1.app.cart.controller.CartController;
import com.hd.v1.app.cart.service.CartService;
import com.hd.v1.app.item.controller.ItemController;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@WebMvcTest(controllers = CartController.class)
@Import(value = com.hd.common.dto.Response.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName(" Item Controller Get Test ")
public class ControllerGetTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CartService cartService;

    private ObjectMapper objectMapper;



    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    @DisplayName("Cart Get 정상")
    void success1() throws Exception {

        //given
        long id = 1L;

        given(cartService.get(any())).willReturn(CartEntity.builder().id(1L).cnt(2L)
                .custEntity(CustEntity.builder().id("id01").build())
                .itemEntity(ItemEntity.builder().id(1L).build())
                .build());

        //when
        ResultActions resultActions= mockMvc.perform(get("/api/cart/get/"+id));

        //verify
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.cnt").value(2L))
//                .andExpect(jsonPath("$.data.price").value(1000L))
                .andDo(print());
        verify(cartService).get(id);
    }
    @Test
    @Order(2)
    @DisplayName("Cart ID_NOT_FOUND")
    void success2() throws Exception {

        //given
        long id = 10L;

        given(cartService.get(any())).willThrow(
                new IdNotFoundException(ErrorCode.ID_NOT_FOUND.getErrorMessage(),
                        ErrorCode.ID_NOT_FOUND)
        );

        //when
        ResultActions resultActions= mockMvc.perform(get("/api/cart/get/"+id));

        //verify
        resultActions.andDo(print());
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value(500))
                .andExpect(jsonPath("$.message").value(ErrorCode.ID_NOT_FOUND.getErrorMessage()))
                .andExpect(jsonPath("$.data.errorCode").value(ErrorCode.ID_NOT_FOUND.getErrorCode()));
    }
}

