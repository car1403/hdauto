package com.hd.v1unit.cart;

import com.hd.common.exception.DataNotFoundException;
import com.hd.common.exception.ErrorCode;
import com.hd.common.exception.IdNotFoundException;
import com.hd.v1.app.cart.repository.CartRepository;
import com.hd.v1.app.cart.service.CartService;
import com.hd.v1.common.entity.CartEntity;
import com.hd.v1.common.entity.CustEntity;
import com.hd.v1.common.entity.ItemEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Cart Service Get Test")
@ExtendWith(MockitoExtension.class)
public class ServiceGetTest {
    @Mock
    CartRepository cartRepository;

    @InjectMocks
    CartService cartService;

    CustEntity custEntity;
    ItemEntity itemEntity1, itemEntity2;
    CartEntity cartEntity1, cartEntity2, cartEntity3;

    @BeforeEach
    public void setup(){
        custEntity = CustEntity.builder().id("id01").pwd("pwd01").name("name01").build();
        itemEntity1 = ItemEntity.builder().name("item01").price(1000L).build();
        itemEntity2 = ItemEntity.builder().name("item02").price(2000L).build();

        cartEntity1 = CartEntity.builder().id(1L).cnt(2L)
                .custEntity(CustEntity.builder().id("id01").build())
                .itemEntity(ItemEntity.builder().id(1L).build())
                .build();
        cartEntity2 = CartEntity.builder().id(2L).cnt(3L)
                .custEntity(CustEntity.builder().id("id01").build())
                .itemEntity(ItemEntity.builder().id(2L).build())
                .build();
        cartEntity3 = CartEntity.builder().id(1L).cnt(3L)
                .custEntity(CustEntity.builder().id("id01").build())
                .itemEntity(ItemEntity.builder().id(1L).build())
                .build();
    }

    @Test
    @Order(1)
    @DisplayName("Cart Get ")
    void success1() {
        // given

        // stub
        when(cartRepository.findById(any())).thenReturn(Optional.of(cartEntity1));
        // when
        CartEntity result = cartService.get(cartEntity1.getId());
        // then
        assertThat(result.getId()).isEqualTo(cartEntity1.getId());
        assertThat(result.getCnt()).isEqualTo(cartEntity1.getCnt());
    }

    @Test
    @Order(2)
    @DisplayName("ID Not Found")
    void success2() {
        // stub
        Long id = 10L;
        // when

        // then
        assertThatThrownBy(() -> cartService.get(id))
                .isInstanceOf(IdNotFoundException.class)
                .hasMessage(ErrorCode.ID_NOT_FOUND.getErrorMessage()); // Exception 객체가 가지고있는 메시지 검증


    }
}
