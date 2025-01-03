package com.hd.v1unit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hd.common.exception.DataNotFoundException;
import com.hd.common.exception.ErrorCode;
import com.hd.v1.app.item.controller.ItemController;
import com.hd.v1.app.item.dto.ItemRequestDto;
import com.hd.v1.app.item.service.ItemService;
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
@WebMvcTest(controllers = ItemController.class)
//@AutoConfigureMockMvc
//@SpringBootTest
@Import(value = com.hd.common.dto.Response.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName(" Item Controller Get ALl Test ")
public class ControllerGetAllTest {
    @Autowired
    private MockMvc mockMvc;


    @MockBean
    ItemService itemService;

    private ObjectMapper objectMapper;



    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    @DisplayName("Item Get All 정상")
    void success1() throws Exception {

        //given
        List<ItemEntity> items = new ArrayList<>();
        items.add(ItemRequestDto.builder().id(10L).name("p1").price(100L).build().toEntity());
        items.add(ItemRequestDto.builder().id(11L).name("p2").price(200L).build().toEntity());


        //stub
        given(itemService.getall()).willReturn(items);

        //when
        ResultActions resultActions= mockMvc.perform(get("/api/item/get"));

        //verify
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(10L))
                .andExpect(jsonPath("$.data[0].name").value("p1"))
                .andExpect(jsonPath("$.data[0].price").value(100L))
                .andExpect(jsonPath("$.data[1].id").value(11L))
                .andExpect(jsonPath("$.data[1].name").value("p2"))
                .andExpect(jsonPath("$.data[1].price").value(200L))
                .andDo(print());
        verify(itemService).getall();
    }

    @Test
    @Order(2)
    @DisplayName("Item GetAll Empty 정상")
    void success2() throws Exception {

        //given

        given(itemService.getall()).willThrow(
                new DataNotFoundException(ErrorCode.DATA_DOSE_NOT_EXIST.getErrorMessage(),
                        ErrorCode.DATA_DOSE_NOT_EXIST)
        );

        //when
        ResultActions resultActions= mockMvc.perform(get("/api/item/get"));

        //verify
        resultActions.andDo(print());
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value(500))
                .andExpect(jsonPath("$.message").value(ErrorCode.DATA_DOSE_NOT_EXIST.getErrorMessage()))
                .andExpect(jsonPath("$.data.errorCode").value(ErrorCode.DATA_DOSE_NOT_EXIST.getErrorCode()));
    }
}

