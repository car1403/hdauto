package com.hd.v1unit.cust;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hd.common.exception.ErrorCode;
import com.hd.common.exception.IdDuplicateException;
import com.hd.common.exception.NameDuplicateException;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(controllers = CustController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Cust Controller Save Test")
@Import(value = com.hd.common.dto.Response.class)
public class ControllerSaveTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    CustService custService;


    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup(){
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    @DisplayName("Cust Save Test")
    public void test1() throws Exception {
        // given
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
        // stub        when(itemRepository.save(any())).thenReturn(itemEntity);
        given(custService.save(any())).willReturn(
                CustEntity.builder()
                        .id(id)
                        .pwd(pwd)
                        .name(name)
                        .build()
        );
        // when
        ResultActions resultActions =
                mockMvc.perform(post("/api/cust/add")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(body));
        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data.id").value(id));
        resultActions.andExpect(jsonPath("$.data.pwd").value(pwd));
        resultActions.andExpect(jsonPath("$.data.name").value(name));
        resultActions.andDo(print());
    }

    // Validated Id
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
                mockMvc.perform(post("/api/cust/add")
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
                mockMvc.perform(post("/api/cust/add")
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
                mockMvc.perform(post("/api/cust/add")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(body));
        // then
        resultActions.andDo(print());
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value(400))
                .andExpect(jsonPath("$.error.[0].field").value("name"))
                .andExpect(jsonPath("$.error.[0].message").value("Name cannot be empty"));
    }

    // Duplicated Name Exception
    @Test
    @Order(5)
    @DisplayName("Duplicated Id Test")
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
        given(custService.save(any())).willThrow(
            new IdDuplicateException(ErrorCode.ID_DUPLICATED.getErrorMessage(),
                    ErrorCode.ID_DUPLICATED)
        );
        // when
        ResultActions resultActions =
                mockMvc.perform(post("/api/cust/add")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(body));
        // then
        resultActions.andDo(print());
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value(500))
                .andExpect(jsonPath("$.message").value(ErrorCode.ID_DUPLICATED.getErrorMessage()))
                .andExpect(jsonPath("$.data.errorCode").value(ErrorCode.ID_DUPLICATED.getErrorCode()));
    }

}
