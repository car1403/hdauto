package com.hd.v1unit.cart;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Cart Service Add Test")
@ExtendWith(MockitoExtension.class)
public class ServiceUpdateTest {
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
    @DisplayName("New Cart Add")
    @Order(1)
    public void test1(){
        // given
        //stub
        when(cartRepository.save(any())).thenReturn(cartEntity1);
        // when
        CartEntity result = cartService.save(cartEntity1);
        // then verify
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getCnt()).isEqualTo(2);
        assertThat(result.getItem().getId()).isEqualTo(1);
        assertThat(result.getCust().getId()).isEqualTo("id01");
    }
    @Test
    @DisplayName("New Cart Add Plus")
    @Order(2)
    public void test2(){
        // given
        //stub
        when(cartRepository.findById(any())).thenReturn(Optional.of(cartEntity1));
        when(cartRepository.save(any())).thenReturn(cartEntity3);
        // when
        CartEntity result = cartService.save(cartEntity3);
        // then verify
        assertThat(result).isNotNull();
        log.info(result.getCnt().toString());
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getCnt()).isEqualTo(5);
        assertThat(result.getItem().getId()).isEqualTo(1);
        assertThat(result.getCust().getId()).isEqualTo("id01");
    }
//
//    @Test
//    @DisplayName("Id Duplicated Exception")
//    @Order(2)
//    public void test2(){
//        //given
//        CustEntity custEntity = CustEntity.builder()
//                .id(id)
//                .pwd(pwd)
//                .name(name)
//                .build();
//        CustEntity newCustEntity = CustEntity.builder()
//                .id(id)
//                .pwd(pwd)
//                .name(name)
//                .build();
//        //stub
//        when(custRepository.findById(any())).thenReturn(
//                Optional.of(custEntity)
//        );
//        // when
//
//        // then
//        assertThatThrownBy(() -> custService.save(newCustEntity))
//                .isInstanceOf(IdDuplicateException.class)
//                .hasMessageContaining(ErrorCode.ID_DUPLICATED.getErrorMessage());
//    }
}
