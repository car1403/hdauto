package com.hd.v1unit.cust;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hd.common.exception.ErrorCode;
import com.hd.common.exception.IdNotFoundException;
import com.hd.v1.app.cust.controller.CustController;
import com.hd.v1.app.cust.service.CustService;
import com.hd.v1.app.item.controller.ItemController;
import com.hd.v1.app.item.service.ItemService;
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
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@WebMvcTest(controllers = CustController.class)
@Import(value = com.hd.common.dto.Response.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName(" Cust Controller Get Test ")
public class ControllerGetTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CustService custService;


    private ObjectMapper objectMapper;



    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    @DisplayName("Cust Get 정상")
    void success1() throws Exception {

        //given
        String id = "id01";
        String pwd = "pwd01";
        String name = "name01";

        given(custService.get(any())).willReturn(CustEntity.builder()
                .id(id)
                .pwd(pwd)
                .name(name)
                .build());

        //when
        ResultActions resultActions= mockMvc.perform(get("/api/cust/get/"+id));

        //verify
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(id))
                .andExpect(jsonPath("$.data.name").value(name))
                .andExpect(jsonPath("$.data.pwd").value(pwd))
                .andDo(print());
        verify(custService).get(id);
    }
    @Test
    @Order(2)
    @DisplayName("Cust Get Empty 정상")
    void success2() throws Exception {

        //given
        String id = "id11";

        given(custService.get(any())).willThrow(
                new IdNotFoundException(ErrorCode.ID_NOT_FOUND.getErrorMessage(),
                        ErrorCode.ID_NOT_FOUND)
        );

        //when
        ResultActions resultActions= mockMvc.perform(get("/api/cust/get/"+id));

        //verify
        resultActions.andDo(print());
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value(500))
                .andExpect(jsonPath("$.message").value(ErrorCode.ID_NOT_FOUND.getErrorMessage()))
                .andExpect(jsonPath("$.data.errorCode").value(ErrorCode.ID_NOT_FOUND.getErrorCode()));
    }
}

