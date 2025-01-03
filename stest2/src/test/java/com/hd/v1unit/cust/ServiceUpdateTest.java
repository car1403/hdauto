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
@DisplayName(" Cust Service Update Test ")
@ExtendWith(MockitoExtension.class)
class ServiceUpdateTest {
    @Mock
    CustRepository custRepository;

    @InjectMocks
    CustService custService;


    String id;
    String pwd;
    String name;


    String newPwd;
    String newName;

    @BeforeEach
    void setup() {
        id = "id01";
        pwd = "pwd01";
        name = "james01";

        newPwd = "pwd02";
        newName = "james02";
    }

    @Test
    @DisplayName("Cust 정보 수정")
    void success1() {
        //given
        CustEntity custEntity = CustEntity.builder()
                .id(id)
                .pwd(pwd)
                .name(name)
                .build();
        CustEntity newCustEntity = CustEntity.builder()
                .id(id)
                .pwd(newPwd)
                .name(newName)
                .build();

        //stub
        when(custRepository.findById(id)).thenReturn(Optional.of(custEntity));
        when(custRepository.save(any(CustEntity.class))).thenReturn(newCustEntity);

        // when
        CustEntity result = custService.modify(newCustEntity);

        //verify

        assertThat(result.getPwd()).isEqualTo(newPwd);
        assertThat(result.getName()).isEqualTo(newName);

    }

    @Test
    @DisplayName("아이디에 해당되는 게시물이 없는 경우")
    void fail1() {
        //given
        CustEntity custEntity = CustEntity.builder()
                .id(id)
                .pwd(pwd)
                .name(name)
                .build();
        //stubx
//                when(itemRepository.findById(undefinedId)).thenThrow(
//                        new IllegalArgumentException("id를 찾을 수 없습니다."));

        //when

        //verify
        assertThatThrownBy(() -> custService.modify(custEntity))
                .isInstanceOf(IdNotFoundException.class)
                .hasMessage(ErrorCode.ID_NOT_FOUND.getErrorMessage()); // Exception 객체가 가지고있는 메시지 검증

    }

}
