package com.hd.v1unit.cust;

import com.hd.common.exception.DataNotFoundException;
import com.hd.common.exception.ErrorCode;
import com.hd.v1.app.cust.repository.CustRepository;
import com.hd.v1.app.cust.service.CustService;
import com.hd.v1.app.item.repository.ItemRepository;
import com.hd.v1.app.item.service.ItemService;
import com.hd.v1.common.entity.CustEntity;
import com.hd.v1.common.entity.ItemEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

//@SpringBootTest

@Slf4j
@TestMethodOrder(MethodOrderer.DisplayName.class)
@DisplayName(" Cust Service Get All Test ")
@ExtendWith(MockitoExtension.class)
class ServiceGetAllTest {
    @Mock
    CustRepository custRepository;

    @InjectMocks
    CustService custService;


    @Test
    @DisplayName("모든 Cust get")
    void success1() {
        // given
        List<CustEntity> custs = new ArrayList<>();
        custs.add(CustEntity.builder().id("id01").name("p1").pwd("pwd01").build());
        custs.add(CustEntity.builder().id("id02").name("p2").pwd("pwd02").build());
        custs.add(CustEntity.builder().id("id03").name("p3").pwd("pwd03").build());
        custs.add(CustEntity.builder().id("id04").name("p4").pwd("pwd04").build());
        custs.add(CustEntity.builder().id("id05").name("p5").pwd("pwd05").build());

        // stub
        when(custRepository.findAll()).thenReturn(custs);

        // when
        List<CustEntity> custList = custService.getall();

        // then
        assertThat(custList.size()).isEqualTo(5);
        assertThat(custList.get(0).getId()).isEqualTo(custs.get(0).getId());
        assertThat(custList.get(1).getId()).isEqualTo(custs.get(1).getId());
        assertThat(custList.get(0).getName()).isEqualTo(custs.get(0).getName());
        assertThat(custList.get(1).getName()).isEqualTo(custs.get(1).getName());

    }

    @Test
    @DisplayName("데이터가 존재 하지 않을때")
    void getItemSuccess2() {
        // stub

        // when

        // then
        assertThatThrownBy(() -> custService.getall())
                .isInstanceOf(DataNotFoundException.class)
                .hasMessage(ErrorCode.DATA_DOSE_NOT_EXIST.getErrorMessage()); // Exception 객체가 가지고있는 메시지 검증


    }



}
