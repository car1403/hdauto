package com.hd.v1integrate.cust;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hd.common.exception.ErrorCode;
import com.hd.v1.app.cust.dto.CustRequestDto;
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
@DisplayName("Cust Integration Test")
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
        return "http://localhost:" + port +"/api/cust/"+action;
    }

    @Test
    @DisplayName("Cust 등록 정상")
    @Order(1)
    public void test1() throws JsonProcessingException {
        // given
        String id = "id01";
        String pwd = "pwd01";
        String name = "james01";

        CustRequestDto custRequestDto =
                CustRequestDto.builder().id(id).name(name).pwd(pwd).build();
        HttpEntity<CustRequestDto> requestEntity = new HttpEntity<>(custRequestDto);
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
        assertThat(jsonNode.get("data").get("id").asText()).isEqualTo(id);
        assertThat(jsonNode.get("data").get("pwd").asText()).isEqualTo(pwd);
        assertThat(jsonNode.get("data").get("name").asText()).isEqualTo(name);
    }


    // Item 정보 수정 정상
    @Test
    @DisplayName("Cust 수정 정상")
    @Order(2)
    public void test2() throws JsonProcessingException {
        // given
        String id = "id01";
        String pwd = "pwd01";
        String name = "james22";

        CustRequestDto custRequestDto =
                CustRequestDto.builder().id(id).name(name).pwd(pwd).build();
        HttpEntity<CustRequestDto> requestEntity = new HttpEntity<>(custRequestDto);

        // when

        ResponseEntity<String> responseEntity =
                testRestTemplate.exchange(getBaseUrl("update"),HttpMethod.PATCH,requestEntity,String.class);


        // verify
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(jsonNode.get("data").get("id").asText()).isEqualTo(id);
        assertThat(jsonNode.get("data").get("pwd").asText()).isEqualTo(pwd);
        assertThat(jsonNode.get("data").get("name").asText()).isEqualTo(name);
    }
    // Item 정보 수정 시 IdNotFoundException 정상

    // 특정 Id 조회 정상
    @Test
    @DisplayName("Cust ID 조회")
    @Order(3)
    public void test5() throws JsonProcessingException {
        // given
        String id = "id01";
        String pwd = "pwd01";
        String name = "james22";

        // when
        ResponseEntity<String> responseEntity =
                testRestTemplate.exchange(getBaseUrl("get/")+id, HttpMethod.GET,null,String.class);


        // then
        log.info(responseEntity.getBody());
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(jsonNode.get("data").get("id").asText()).isEqualTo(id);
        assertThat(jsonNode.get("data").get("pwd").asText()).isEqualTo(pwd);
        assertThat(jsonNode.get("data").get("name").asText()).isEqualTo(name);

    }


    // 특정 Id 조회 실패 IdNotFoundException 정상
    @Test
    @DisplayName("Cust ID 조회 실패 IdNotFoundException")
    @Order(4)
    public void test4() throws JsonProcessingException {
        // given
        String id = "id11";

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
