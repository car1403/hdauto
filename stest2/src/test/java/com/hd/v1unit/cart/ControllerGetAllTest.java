package com.hd.v1unit.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hd.common.exception.DataNotFoundException;
import com.hd.common.exception.ErrorCode;
import com.hd.v1.app.cart.controller.CartController;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@WebMvcTest(controllers = CartController.class)
//@AutoConfigureMockMvc
//@SpringBootTest
@Import(value = com.hd.common.dto.Response.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName(" Cart Controller Get ALl Test ")
public class ControllerGetAllTest {
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
    @DisplayName("Cart Get All 정상")
    void success1() throws Exception {

        //given
        List<CartEntity> carts = new ArrayList<>();
        carts.add( CartEntity.builder().id(1L).cnt(1L)
                .custEntity(CustEntity.builder().id("id01").build())
                .itemEntity(ItemEntity.builder().id(1L).build())
                .build());
        carts.add( CartEntity.builder().id(2L).cnt(2L)
                .custEntity(CustEntity.builder().id("id01").build())
                .itemEntity(ItemEntity.builder().id(2L).build())
                .build());


        //stub
        given(cartService.getall()).willReturn(carts);

        //when
        ResultActions resultActions= mockMvc.perform(get("/api/cart/get"));

        //verify
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].cnt").value(1L))
                .andExpect(jsonPath("$.data[1].id").value(2L))
                .andExpect(jsonPath("$.data[1].cnt").value(2L))
                .andDo(print());
    }

    @Test
    @Order(2)
    @DisplayName("Cart GetAll Empty 정상")
    void success2() throws Exception {

        //given

        given(cartService.getall()).willThrow(
                new DataNotFoundException(ErrorCode.DATA_DOSE_NOT_EXIST.getErrorMessage(),
                        ErrorCode.DATA_DOSE_NOT_EXIST)
        );

        //when
        ResultActions resultActions= mockMvc.perform(get("/api/cart/get"));

        //verify
        resultActions.andDo(print());
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value(500))
                .andExpect(jsonPath("$.message").value(ErrorCode.DATA_DOSE_NOT_EXIST.getErrorMessage()))
                .andExpect(jsonPath("$.data.errorCode").value(ErrorCode.DATA_DOSE_NOT_EXIST.getErrorCode()));
    }
}

