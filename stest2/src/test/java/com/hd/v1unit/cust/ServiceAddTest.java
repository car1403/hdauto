package com.hd.v1unit.cust;

import com.hd.common.exception.ErrorCode;
import com.hd.common.exception.IdDuplicateException;
import com.hd.common.exception.NameDuplicateException;
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

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Cust Service Add Test")
@ExtendWith(MockitoExtension.class)
public class ServiceAddTest {
    @Mock
    CustRepository custRepository;

    @InjectMocks
    CustService custService;

    String id;
    String pwd;
    String name;


    @BeforeEach
    public void setup(){
        id = "id01";
        pwd = "pwd01";
        name = "james01";
    }

    @Test
    @DisplayName("New Cust Add")
    @Order(1)
    public void test1(){
        // given
        CustEntity custEntity = CustEntity.builder()
                .id(id)
                .pwd(pwd)
                .name(name)
                .build();
        //stub
        when(custRepository.save(any())).thenReturn(custEntity);
        // when
        CustEntity result = custService.save(custEntity);
        // then verify
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getPwd()).isEqualTo(pwd);
        assertThat(result.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("Id Duplicated Exception")
    @Order(2)
    public void test2(){
        //given
        CustEntity custEntity = CustEntity.builder()
                .id(id)
                .pwd(pwd)
                .name(name)
                .build();
        CustEntity newCustEntity = CustEntity.builder()
                .id(id)
                .pwd(pwd)
                .name(name)
                .build();
        //stub
        when(custRepository.findById(any())).thenReturn(
                Optional.of(custEntity)
        );
        // when

        // then
        assertThatThrownBy(() -> custService.save(newCustEntity))
                .isInstanceOf(IdDuplicateException.class)
                .hasMessageContaining(ErrorCode.ID_DUPLICATED.getErrorMessage());
    }
}
