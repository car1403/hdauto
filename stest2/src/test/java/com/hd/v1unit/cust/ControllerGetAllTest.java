package com.hd.v1unit.cust;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hd.common.exception.DataNotFoundException;
import com.hd.common.exception.ErrorCode;
import com.hd.v1.app.cust.controller.CustController;
import com.hd.v1.app.cust.dto.CustRequestDto;
import com.hd.v1.app.cust.service.CustService;
import com.hd.v1.app.item.controller.ItemController;
import com.hd.v1.app.item.dto.ItemRequestDto;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@WebMvcTest(controllers = CustController.class)
//@AutoConfigureMockMvc
//@SpringBootTest
@Import(value = com.hd.common.dto.Response.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName(" Cust Controller Get ALl Test ")
public class ControllerGetAllTest {
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
    @DisplayName("Cust Get All 정상")
    void success1() throws Exception {

        //given
        List<CustEntity> custs = new ArrayList<>();
        custs.add( CustEntity.builder()
                .id("id01")
                .pwd("pwd01")
                .name("james01")
                .build());
        custs.add( CustEntity.builder()
                .id("id02")
                .pwd("pwd02")
                .name("james02")
                .build());


        //stub
        given(custService.getall()).willReturn(custs);

        //when
        ResultActions resultActions= mockMvc.perform(get("/api/cust/get"));

        //verify
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value("id01"))
                .andExpect(jsonPath("$.data[0].name").value("james01"))
                .andExpect(jsonPath("$.data[0].pwd").value("pwd01"))
                .andExpect(jsonPath("$.data[1].id").value("id02"))
                .andExpect(jsonPath("$.data[1].name").value("james02"))
                .andExpect(jsonPath("$.data[1].pwd").value("pwd02"))
                .andDo(print());
        verify(custService).getall();
    }

    @Test
    @Order(2)
    @DisplayName("Cust GetAll Empty 정상")
    void success2() throws Exception {

        //given

        given(custService.getall()).willThrow(
                new DataNotFoundException(ErrorCode.DATA_DOSE_NOT_EXIST.getErrorMessage(),
                        ErrorCode.DATA_DOSE_NOT_EXIST)
        );

        //when
        ResultActions resultActions= mockMvc.perform(get("/api/cust/get"));

        //verify
        resultActions.andDo(print());
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value(500))
                .andExpect(jsonPath("$.message").value(ErrorCode.DATA_DOSE_NOT_EXIST.getErrorMessage()))
                .andExpect(jsonPath("$.data.errorCode").value(ErrorCode.DATA_DOSE_NOT_EXIST.getErrorCode()));
    }
}

