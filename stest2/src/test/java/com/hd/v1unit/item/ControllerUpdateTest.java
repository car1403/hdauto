package com.hd.v1unit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hd.common.exception.ErrorCode;
import com.hd.common.exception.IdNotFoundException;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@WebMvcTest(controllers = ItemController.class)
//@AutoConfigureMockMvc
//@SpringBootTest
@Import(value = com.hd.common.dto.Response.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName(" Item Controller Update Test ")
public class ControllerUpdateTest {
    @Autowired
    private MockMvc mockMvc;

//    @MockBean
//    Response response;

    @MockBean
    ItemService itemService;

    private ObjectMapper objectMapper;



    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    @DisplayName("Item Update 정상")
    void success1() throws Exception {

        //given
        ItemRequestDto reqDto = ItemRequestDto.builder().id(100L).name("p1").price(1000L).build();
        String body = new ObjectMapper().writeValueAsString(reqDto); // json 으로 변경한것


        //stub
        given(itemService.modify(any())).willReturn(ItemEntity.builder().id(100L).name("p1").price(1000L).build());

        //when
        ResultActions resultActions= mockMvc.perform(patch("/api/item/update")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body));


        //verify
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(100L))
                .andExpect(jsonPath("$.data.name").value("p1"))
                .andExpect(jsonPath("$.data.price").value(1000L))
                .andDo(print());
        verify(itemService).modify(refEq(reqDto.toEntity()));
        //verify(itemService).modify(reqreqDto.toEntity());
    }
    @Test
    @Order(2)
    @DisplayName("Validated Name Test")
    void validtedName() throws Exception {
        //given
        ItemRequestDto reqDto = ItemRequestDto.builder().name(null).price(100L).build();

        // when
        ResultActions resultActions = mockMvc.perform(
                patch("/api/item/update")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(reqDto))
        );

        // then
        resultActions.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.error.[0].message").value("Name cannot be empty"));
    }
    @Test
    @Order(3)
    @DisplayName("Validated Price Test")
    void validatedPrice() throws Exception {
        //given
        ItemRequestDto reqDto = ItemRequestDto.builder().name("Pants").price(5L).build();

        // when
        ResultActions resultActions = mockMvc.perform(
                patch("/api/item/update")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(reqDto))
        );

        // then
        resultActions.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.error.[0].message").value("10이상이어야함"));
    }

    @Test
    @Order(2)
    @DisplayName("Id Not Found 정상")
    void success2() throws Exception {

        //given
        ItemRequestDto reqDto = ItemRequestDto.builder().id(100L).name("p1").price(1000L).build();
        String body = new ObjectMapper().writeValueAsString(reqDto); // json 으로 변경한것


        //stub
        given(itemService.modify(any())).willThrow(
                new IdNotFoundException(ErrorCode.ID_NOT_FOUND.getErrorMessage(), ErrorCode.ID_NOT_FOUND)
        );

        //when
        ResultActions resultActions= mockMvc.perform(patch("/api/item/update")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body));

        //verify
        resultActions.andDo(print());
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value(500))
                .andExpect(jsonPath("$.message").value(ErrorCode.ID_NOT_FOUND.getErrorMessage()))
                .andExpect(jsonPath("$.data.errorCode").value(ErrorCode.ID_NOT_FOUND.getErrorCode()));
    }

}

