package com.hd.v1unit.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hd.common.exception.ErrorCode;
import com.hd.common.exception.IdNotFoundException;
import com.hd.v1.app.cart.controller.CartController;
import com.hd.v1.app.cart.service.CartService;
import com.hd.v1.app.item.controller.ItemController;
import com.hd.v1.app.item.service.ItemService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@WebMvcTest(controllers = CartController.class)
//@AutoConfigureMockMvc
//@SpringBootTest
@Import(value = com.hd.common.dto.Response.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName(" Item Controller Delete Test ")
public class ControllerDeleteTest {
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
    @DisplayName("Cart Delete 정상")
    void success1() throws Exception {

        //given
        long id = 10L;


        //stub
        given(cartService.remove(any())).willReturn(10L);

        //when
        ResultActions resultActions= mockMvc.perform(delete("/api/cart/"+id));

        //verify
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(10L))
                .andDo(print());
        verify(cartService).remove(id);
    }
    @Test
    @Order(2)
    @DisplayName("ID Not Found 정상")
    void success2() throws Exception {

        //given
        long id = 10L;

        given(cartService.remove(any())).willThrow(
                new IdNotFoundException(ErrorCode.ID_NOT_FOUND.getErrorMessage(),
                        ErrorCode.ID_NOT_FOUND)
        );

        //when
        ResultActions resultActions= mockMvc.perform(delete("/api/cart/"+id));

        //verify
        resultActions.andDo(print());
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value(500))
                .andExpect(jsonPath("$.message").value(ErrorCode.ID_NOT_FOUND.getErrorMessage()))
                .andExpect(jsonPath("$.data.errorCode").value(ErrorCode.ID_NOT_FOUND.getErrorCode()));
    }
}

