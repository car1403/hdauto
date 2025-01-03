package com.hd.v1unit.item;

import com.hd.common.exception.ErrorCode;
import com.hd.common.exception.NameDuplicateException;

import com.hd.v1.app.item.repository.ItemRepository;
import com.hd.v1.app.item.service.ItemService;
import com.hd.v1.common.entity.ItemEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName(" Item Service Add Test")
@ExtendWith(MockitoExtension.class)
public class ServiceAddTest {
    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    ItemService itemService;

    long id;
    String name;
    long price;


    @BeforeEach
    public void setup(){
        id = 1L;
        name = "pants1";
        price = 1000L;
    }

    @Test
    @DisplayName("New Item Add")
    @Order(1)
    public void test1(){
        // given
        ItemEntity itemEntity = ItemEntity.builder()
                .id(id)
                .name(name)
                .price(price)
                .build();
        //stub
        when(itemRepository.save(any())).thenReturn(itemEntity);
        // when
        ItemEntity result = itemService.save(itemEntity);
        // then verify
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getPrice()).isEqualTo(price);
    }

    @Test
    @DisplayName("Name Duplicated Exception")
    @Order(2)
    public void test2(){
        //given
        ItemEntity itemEntity = ItemEntity.builder()
                .id(id)
                .name(name)
                .price(price)
                .build();
        ItemEntity newItemEntity = ItemEntity.builder()
                .name(name)
                .price(price)
                .build();
        //stub
        when(itemRepository.findByName(any())).thenReturn(
                Optional.of(itemEntity)
        );
        // when

        // then
        assertThatThrownBy(() -> itemService.save(newItemEntity))
                .isInstanceOf(NameDuplicateException.class)
                .hasMessageContaining(ErrorCode.NAME_DUPLICATED.getErrorMessage());
    }
}
