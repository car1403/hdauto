package com.hd.v1unit.cart;

import com.hd.common.exception.DataNotFoundException;
import com.hd.common.exception.ErrorCode;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Cart Service Get All CustId Test")
@ExtendWith(MockitoExtension.class)
public class ServiceGetAllCustIdTest {
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
    @DisplayName("Cart Get All CustId")
    void success1() {
        // given
        String custId = "id01";

        List<CartEntity> carts = new ArrayList<>();
        carts.add(CartEntity.builder().id(1L).cnt(1L)
                .custEntity(CustEntity.builder().id("id01").build())
                .itemEntity(ItemEntity.builder().id(1L).build())
                .build());
        carts.add(CartEntity.builder().id(2L).cnt(2L)
                .custEntity(CustEntity.builder().id("id01").build())
                .itemEntity(ItemEntity.builder().id(2L).build())
                .build());
        carts.add(CartEntity.builder().id(3L).cnt(3L)
                .custEntity(CustEntity.builder().id("id01").build())
                .itemEntity(ItemEntity.builder().id(3L).build())
                .build());
        carts.add(CartEntity.builder().id(4L).cnt(4L)
                .custEntity(CustEntity.builder().id("id01").build())
                .itemEntity(ItemEntity.builder().id(4L).build())
                .build());
        carts.add(CartEntity.builder().id(5L).cnt(5L)
                .custEntity(CustEntity.builder().id("id01").build())
                .itemEntity(ItemEntity.builder().id(5L).build())
                .build());

        // stub
        when(cartRepository.findByCustId(any())).thenReturn(carts);

        // when
        List<CartEntity> bookList = cartService.getall(custId);

        // then
        assertThat(bookList.size()).isEqualTo(5);
        assertThat(bookList.get(0).getCnt()).isEqualTo(carts.get(0).getCnt());
        assertThat(bookList.get(1).getCnt()).isEqualTo(carts.get(1).getCnt());
        assertThat(bookList.get(0).getItem().getId()).isEqualTo(carts.get(0).getItem().getId());
        assertThat(bookList.get(1).getItem().getId()).isEqualTo(carts.get(1).getItem().getId());

    }

    @Test
    @Order(2)
    @DisplayName("Data Not Found")
    void success2() {
        // given
        String custId = "id01";
        // stub

        // when

        // then
        assertThatThrownBy(() -> cartService.getall(custId))
                .isInstanceOf(DataNotFoundException.class)
                .hasMessage(ErrorCode.DATA_DOSE_NOT_EXIST.getErrorMessage()); // Exception 객체가 가지고있는 메시지 검증


    }
}
