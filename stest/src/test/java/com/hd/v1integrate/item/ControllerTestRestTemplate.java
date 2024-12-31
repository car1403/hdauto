package com.hd.v1integrate.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hd.v1.item.dto.ItemRequestDto;
import jakarta.websocket.OnClose;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

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
    public void test1(){
        // given
        String name = "p1";
        Long price = 1000L;
        ItemRequestDto itemRequestDto =
                ItemRequestDto.builder().name(name).price(price).build();
        // when
       ResponseEntity<String> responseEntity  =
               testRestTemplate.postForEntity(
                    getBaseUrl("add"),
                    itemRequestDto,
                    String.class
                );
        // then
        log.info(responseEntity.getBody());
    }
}
