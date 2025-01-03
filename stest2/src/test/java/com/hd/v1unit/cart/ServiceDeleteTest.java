package com.hd.v1unit.cart;

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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Cart Service Add Test")
@ExtendWith(MockitoExtension.class)
public class ServiceDeleteTest {
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
    @DisplayName(" Cart Delete")
    @Order(1)
    public void test1(){
        // given
        Long id = 1L;

        //stub
        when(cartRepository.findById(any())).thenReturn(Optional.of(cartEntity1));

        //when
        long resultId = cartService.remove(id);

        //verify
        assertThat(resultId).isEqualTo(id);
    }
    @Test
    @DisplayName("특정 id가 존재하지 않을 때")
    void fail1() {
        // given
        Long undefinedId = 200L;
        //ItemEntity itemEntity = ItemEntity.builder().id(undefinedId).name(name).price(price).build();

        //stub
                /*
                when(itemRepository.findById(undefinedId)).thenThrow(
                        new IllegalArgumentException("id를 찾을 수 없습니다."));
                */
        //when

        //verify
        assertThatThrownBy(() -> cartService.remove(undefinedId))
                .isInstanceOf(IdNotFoundException.class)
                .hasMessage(ErrorCode.ID_NOT_FOUND.getErrorMessage()); // Exception 객체가 가지고있는 메시지 검증

    }
}
