package com.hd.v1unit.cust;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hd.common.exception.ErrorCode;
import com.hd.common.exception.IdDuplicateException;
import com.hd.common.exception.IdNotFoundException;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@WebMvcTest(controllers = CustController.class)
//@AutoConfigureMockMvc
//@SpringBootTest
@Import(value = com.hd.common.dto.Response.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName(" Cust Controller Update Test ")
public class ControllerUpdateTest {
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
    @DisplayName("Cust Update 정상")
    void success1() throws Exception {

        //given
        CustRequestDto reqDto = CustRequestDto.builder()
                .id("id01")
                .pwd("pwd01")
                .name("james01")
                .build();
        String body = new ObjectMapper().writeValueAsString(reqDto); // json 으로 변경한것


        //stub
        given(custService.modify(any())).willReturn(CustEntity.builder()
                .id("id01")
                .pwd("pwd01")
                .name("james01")
                .build());

        //when
        ResultActions resultActions= mockMvc.perform(patch("/api/cust/update")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body));


        //verify
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value("id01"))
                .andExpect(jsonPath("$.data.name").value("james01"))
                .andExpect(jsonPath("$.data.pwd").value("pwd01"))
                .andDo(print());
        verify(custService).modify(refEq(reqDto.toEntity()));
        //verify(itemService).modify(reqreqDto.toEntity());
    }
    @Test
    @Order(2)
    @DisplayName("Validated Id Test")
    public void test2() throws Exception {

        // given
        String id = null;
        String pwd = "pwd01";
        String name = "name01";

        CustRequestDto requestDto =
                CustRequestDto.builder()
                        .id(id)
                        .pwd(pwd)
                        .name(name)
                        .build();
        String body = objectMapper.writeValueAsString(requestDto);
        // when
        ResultActions resultActions =
                mockMvc.perform(patch("/api/cust/update")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(body));
        // then
        resultActions.andDo(print());
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value(400))
                .andExpect(jsonPath("$.error.[0].field").value("id"))
                .andExpect(jsonPath("$.error.[0].message").value("ID cannot be empty"));
    }

    // Validated pwd
    @Test
    @Order(3)
    @DisplayName("Validated Pwd Test")
    public void test3() throws Exception {
        // given
        String id = "id01";
        String pwd = null;
        String name = "name01";

        CustRequestDto requestDto =
                CustRequestDto.builder()
                        .id(id)
                        .pwd(pwd)
                        .name(name)
                        .build();
        String body = objectMapper.writeValueAsString(requestDto);
        // when
        ResultActions resultActions =
                mockMvc.perform(patch("/api/cust/update")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(body));
        // then
        resultActions.andDo(print());
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value(400))
                .andExpect(jsonPath("$.error.[0].field").value("pwd"))
                .andExpect(jsonPath("$.error.[0].message").value("PWD cannot be empty"));
    }
    // Validated Name
    @Test
    @Order(4)
    @DisplayName("Validated Name Test")
    public void test4() throws Exception {
        // given
        String id = "id01";
        String pwd = "pwd01";
        String name = null;

        CustRequestDto requestDto =
                CustRequestDto.builder()
                        .id(id)
                        .pwd(pwd)
                        .name(name)
                        .build();
        String body = objectMapper.writeValueAsString(requestDto);
        // when
        ResultActions resultActions =
                mockMvc.perform(patch("/api/cust/update")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(body));
        // then
        resultActions.andDo(print());
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value(400))
                .andExpect(jsonPath("$.error.[0].field").value("name"))
                .andExpect(jsonPath("$.error.[0].message").value("Name cannot be empty"));
    }

    @Test
    @Order(5)
    @DisplayName("ID Not Found")
    public void test5() throws Exception {
        //given
        String id = "id01";
        String pwd = "pwd01";
        String name = "name01";

        CustRequestDto requestDto =
                CustRequestDto.builder()
                        .id(id)
                        .pwd(pwd)
                        .name(name)
                        .build();
        String body = objectMapper.writeValueAsString(requestDto);
        // stub
        given(custService.modify(any())).willThrow(
                new IdNotFoundException(ErrorCode.ID_NOT_FOUND.getErrorMessage(),
                        ErrorCode.ID_NOT_FOUND)
        );
        // when
        ResultActions resultActions =
                mockMvc.perform(patch("/api/cust/update")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(body));
        // then
        resultActions.andDo(print());
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value(500))
                .andExpect(jsonPath("$.message").value(ErrorCode.ID_NOT_FOUND.getErrorMessage()))
                .andExpect(jsonPath("$.data.errorCode").value(ErrorCode.ID_NOT_FOUND.getErrorCode()));
    }

}

