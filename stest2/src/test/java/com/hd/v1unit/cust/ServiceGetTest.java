package com.hd.v1unit.cust;

import com.hd.common.exception.ErrorCode;
import com.hd.common.exception.IdNotFoundException;
import com.hd.v1.app.cust.repository.CustRepository;
import com.hd.v1.app.cust.service.CustService;
import com.hd.v1.app.item.repository.ItemRepository;
import com.hd.v1.app.item.service.ItemService;
import com.hd.v1.common.entity.CustEntity;
import com.hd.v1.common.entity.ItemEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
//@SpringBootTest

@Slf4j
@TestMethodOrder(MethodOrderer.DisplayName.class)
@DisplayName("Cust  Service Get Test ")
@ExtendWith(MockitoExtension.class)
class ServiceGetTest {
    @Mock
    CustRepository custRepository;

    @InjectMocks
    CustService custService;

    String id;
    String pwd;
    String name;

    @BeforeEach
    void setup() {
        id = "id01";
        pwd = "pwd01";
        name = "james01";
    }

    @Test
    @DisplayName("정상 케이스")
    void success1() {
        // give
        CustEntity custEntity = CustEntity.builder()
                .id(id)
                .pwd(pwd)
                .name(name)
                .build();

        //stub
        when(custRepository.findById(any())).thenReturn(Optional.of(custEntity));

        // when

        CustEntity result = custService.get(id);

        //  verify
        assertThat(result.getId()).isEqualTo(custEntity.getId());
        assertThat(result.getPwd()).isEqualTo(custEntity.getPwd());
        assertThat(result.getName()).isEqualTo(custEntity.getName());
    }
    @Test
    @DisplayName("특정 id가 존재하지 않을때")
    void fail1() {
        // give
        String undefinedId = "id02";

        // when

        //  verify

        assertThatThrownBy(() -> custService.get(undefinedId))
                .isInstanceOf(IdNotFoundException.class)
                .hasMessage(ErrorCode.ID_NOT_FOUND.getErrorMessage()); // Exception 객체가 가지고있는 메시지 검증

    }



}
