package com.hd.v1integrate.cart;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hd.common.exception.ErrorCode;
import com.hd.v1.app.cart.dto.CartRequestDto;
import com.hd.v1.app.cust.dto.CustRequestDto;
import com.hd.v1.app.item.dto.ItemRequestDto;
import com.hd.v1.common.entity.CustEntity;
import com.hd.v1.common.entity.ItemEntity;
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
@DisplayName("Cart Integration Test")
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
        return "http://localhost:" + port +"/api/cart/"+action;
    }

    @Test
    @DisplayName("Cart 등록 정상")
    @Order(1)
    public void test1() throws JsonProcessingException {
        // given
        CustEntity custEntity = CustEntity.builder().id("id01").pwd("pwd01").name("james01").build();
        ItemEntity itemEntity1 = ItemEntity.builder().name("item01").price(1000L).build();
        ItemEntity itemEntity2 = ItemEntity.builder().name("item02").price(2000L).build();

        CustRequestDto custRequestDto = CustRequestDto.builder().id("id01").pwd("pwd01").name("james01").build();
        ItemRequestDto itemRequestDto1 = ItemRequestDto.builder().name("item01").price(1000L).build();
        ItemRequestDto itemRequestDto2 = ItemRequestDto.builder().name("item01").price(2000L).build();

        HttpEntity<CustRequestDto> custRequestEntity = new HttpEntity<>(custRequestDto);
        HttpEntity<ItemRequestDto> itemRequestEntity1 = new HttpEntity<>(itemRequestDto1);
        HttpEntity<ItemRequestDto> itemRequestEntity2 = new HttpEntity<>(itemRequestDto2);
        ResponseEntity<String> custResponseEntity = testRestTemplate.exchange("http://localhost:" + port +"/api/cust/add", HttpMethod.POST,custRequestEntity,String.class);
        ResponseEntity<String> itemResponseEntity1 = testRestTemplate.exchange("http://localhost:" + port +"/api/item/add", HttpMethod.POST,itemRequestEntity1,String.class);
        ResponseEntity<String> itemResponseEntity2 = testRestTemplate.exchange("http://localhost:" + port +"/api/item/add", HttpMethod.POST,itemRequestEntity2,String.class);

        JsonNode jsonNodeCust = objectMapper.readTree(custResponseEntity.getBody());
        JsonNode jsonNodeItem1 = objectMapper.readTree(itemResponseEntity1.getBody());
        JsonNode jsonNodeItem2 = objectMapper.readTree(itemResponseEntity2.getBody());

        log.info(custResponseEntity.getBody());
        log.info(itemResponseEntity1.getBody());
        log.info(jsonNodeCust.get("data").get("id").asText());
        log.info(jsonNodeItem1.get("data").get("id").asLong()+"");
        CartRequestDto requestDto =
                CartRequestDto.builder().cnt(2L)
                        .custId(jsonNodeCust.get("data").get("id").asText())
                        .itemId(jsonNodeItem1.get("data").get("id").asLong())
                        .build();
        HttpEntity<CartRequestDto> requestEntity = new HttpEntity<>(requestDto);
        // when
        ResponseEntity<String> responseEntity =
                testRestTemplate.exchange(getBaseUrl("add"), HttpMethod.POST,requestEntity,String.class);

        // then
        log.info(responseEntity.getBody());
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(jsonNode.get("data").get("id").asLong()).isEqualTo(1L);
        assertThat(jsonNode.get("data").get("cnt").asLong()).isEqualTo(2L);

    }

//    // Item 정보 수정 정상
    @Test
    @DisplayName("Cart 수정 정상")
    @Order(2)
    public void test2() throws JsonProcessingException {
        // given
        String custId = "id01";
        Long itemId = 1L;
        Long cartId = 1L;
        CartRequestDto requestDto =
                CartRequestDto.builder().id(cartId).cnt(5L)
                        .custId(custId)
                        .itemId(itemId)
                        .build();
        HttpEntity<CartRequestDto> requestEntity = new HttpEntity<>(requestDto);

        // when

        ResponseEntity<String> responseEntity =
                testRestTemplate.exchange(getBaseUrl("update"),HttpMethod.PATCH,requestEntity,String.class);
        log.info(responseEntity.getBody());
        // verify
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(jsonNode.get("data").get("id").asLong()).isEqualTo(cartId);
        assertThat(jsonNode.get("data").get("cnt").asLong()).isEqualTo(5L);

    }
    // Item 정보 수정 시 IdNotFoundException 정상

    // 특정 Id 조회 정상
    @Test
    @DisplayName("Cart ID 조회")
    @Order(3)
    public void test3() throws JsonProcessingException {
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
    @DisplayName("Cart ID 조회 실패 IdNotFoundException")
    @Order(4)
    public void test4() throws JsonProcessingException {
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
    // 특정 Id 조회 정상
    @Test
    @DisplayName("Cart 특정 cust ID 조회")
    @Order(5)
    public void test5() throws JsonProcessingException {
        // given
        String custId = "id01";

        // when
        ResponseEntity<String> responseEntity =
                testRestTemplate.exchange(getBaseUrl("get/cust/")+custId, HttpMethod.GET,null,String.class);


        // then
        log.info(responseEntity.getBody());
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);


    }
    @Test
    @DisplayName("Cart 삭제")
    @Order(6)
    public void test6() throws JsonProcessingException {
        // given
        Long id = 1L;

        // when
        ResponseEntity<String> responseEntity =
                testRestTemplate.exchange(getBaseUrl(id+""), HttpMethod.DELETE,null,String.class);


        // then
        log.info(responseEntity.getBody());
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(jsonNode.get("data").asLong()).isEqualTo(id);

    }

}
