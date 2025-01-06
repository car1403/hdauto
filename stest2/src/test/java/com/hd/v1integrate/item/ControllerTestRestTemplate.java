package com.hd.v1integrate.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hd.common.exception.ErrorCode;

import com.hd.v1.app.item.dto.ItemRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Item Integration Test")
@ActiveProfiles("dev")
public class ControllerTestRestTemplate {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup(){
        objectMapper = new ObjectMapper();
    }

    public String getBaseUrl(String action){
        return "http://localhost:" + port +"/api/item/"+action;
    }

    @Test
    @DisplayName("Item 등록 정상")
    @Order(1)
    public void test1() throws JsonProcessingException {
        // given
        String name = "p1";
        Long price = 1000L;
        ItemRequestDto itemRequestDto =
                ItemRequestDto.builder().name(name).price(price).build();
        HttpEntity<ItemRequestDto> requestEntity = new HttpEntity<>(itemRequestDto);
        // when
        ResponseEntity<String> responseEntity =
                testRestTemplate.exchange(getBaseUrl("add"), HttpMethod.POST,requestEntity,String.class);

//        ResponseEntity<String> responseEntity  =
//               testRestTemplate.postForEntity(
//                    getBaseUrl("add"),
//                    itemRequestDto,
//                    String.class
//                );
        // then
        log.info(responseEntity.getBody());
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
        log.info(jsonNode.get("data").get("id").toString());
        log.info(jsonNode.get("data").get("name").toString());
        log.info(jsonNode.get("data").get("price").toString());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(jsonNode.get("data").get("name").asText()).isEqualTo(name);
        assertThat(jsonNode.get("data").get("price").asLong()).isEqualTo(price);

    }

    // Item Name Duplicated Exception
    @Test
    @DisplayName("Item Name Duplicated Exception")
    @Order(2)
    public void test2() throws JsonProcessingException {
        // given
        String name = "p1";
        Long price = 1000L;
        ItemRequestDto itemRequestDto =
                ItemRequestDto.builder().name(name).price(price).build();
        HttpEntity<ItemRequestDto> requestEntity = new HttpEntity<>(itemRequestDto);
        // when
        ResponseEntity<String> responseEntity =
                testRestTemplate.exchange(getBaseUrl("add"), HttpMethod.POST,requestEntity,String.class);

//        ResponseEntity<String> responseEntity  =
//               testRestTemplate.postForEntity(
//                    getBaseUrl("add"),
//                    itemRequestDto,
//                    String.class
//                );
        // then
        log.info(responseEntity.getBody());
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(jsonNode.get("state").asInt()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(jsonNode.get("message").asText()).isEqualTo(ErrorCode.NAME_DUPLICATED.getErrorMessage());
        assertThat(jsonNode.get("data").get("errorCode").asText()).isEqualTo(ErrorCode.NAME_DUPLICATED.getErrorCode());
        assertThat(jsonNode.get("data").get("errorMessage").asText()).isEqualTo(ErrorCode.NAME_DUPLICATED.getErrorMessage());

    }

    // Item 정보 수정 정상
    @Test
    @DisplayName("Item 수정 정상")
    @Order(3)
    public void test3() throws JsonProcessingException {
        // given
        long id = 1L;
        String name = "p22";
        long price = 2000L;
        ItemRequestDto requestDto = ItemRequestDto.builder().id(id).name(name).price(price).build();
        HttpEntity<ItemRequestDto> requestEntity = new HttpEntity<>(requestDto);

        // when

        ResponseEntity<String> responseEntity =
                testRestTemplate.exchange(getBaseUrl("update"),HttpMethod.PATCH,requestEntity,String.class);
        log.info("Update ResponseEntity -------------"+responseEntity.toString());
        log.info("Update Body -------------"+responseEntity.getBody().toString());

        // verify
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());

    }
    // Item 정보 수정 시 IdNotFoundException 정상

    // 특정 Id 조회 정상
    @Test
    @DisplayName("Item ID 조회")
    @Order(5)
    public void test5() throws JsonProcessingException {
        // given
        Long id = 1L;

        // when
        ResponseEntity<String> responseEntity =
                testRestTemplate.exchange(getBaseUrl("get/")+id, HttpMethod.GET,null,String.class);


        // then
        log.info(responseEntity.getBody());
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(jsonNode.get("data").get("id").asLong()).isEqualTo(id);

    }


    // 특정 Id 조회 실패 IdNotFoundException 정상
    @Test
    @DisplayName("Item ID 조회 실패 IdNotFoundException")
    @Order(6)
    public void test6() throws JsonProcessingException {
        // given
        Long id = 2L;

        // when
        ResponseEntity<String> responseEntity =
                testRestTemplate.exchange(getBaseUrl("get/")+id,
                        HttpMethod.GET,null,String.class);


        // then
        log.info(responseEntity.getBody());
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(jsonNode.get("state").asInt()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(jsonNode.get("message").asText()).isEqualTo(ErrorCode.ID_NOT_FOUND.getErrorMessage());
        assertThat(jsonNode.get("data").get("errorCode").asText()).isEqualTo(ErrorCode.ID_NOT_FOUND.getErrorCode());
        assertThat(jsonNode.get("data").get("errorMessage").asText()).isEqualTo(ErrorCode.ID_NOT_FOUND.getErrorMessage());


    }


}
