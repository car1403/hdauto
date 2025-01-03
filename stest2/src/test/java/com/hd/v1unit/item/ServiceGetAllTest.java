package com.hd.v1unit.item;

import com.hd.common.exception.DataNotFoundException;
import com.hd.common.exception.ErrorCode;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hd.v1.app.item.repository.ItemRepository;
import com.hd.v1.app.item.service.ItemService;
import com.hd.v1.common.entity.ItemEntity;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

//@SpringBootTest

@Slf4j
@TestMethodOrder(MethodOrderer.DisplayName.class)
@DisplayName(" Item Service Get All Test ")
@ExtendWith(MockitoExtension.class)
class ServiceGetAllTest {
    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    ItemService itemService;

    @Test
    @DisplayName("모든 Item get")
    void success1() {
        // given
        List<ItemEntity> books = new ArrayList<>();
        books.add(ItemEntity.builder().id(1L).name("p1").price(1000L).build());
        books.add(ItemEntity.builder().id(2L).name("p2").price(2000L).build());
        books.add(ItemEntity.builder().id(3L).name("p3").price(3000L).build());
        books.add(ItemEntity.builder().id(4L).name("p4").price(4000L).build());
        books.add(ItemEntity.builder().id(5L).name("p5").price(5000L).build());

        // stub
        when(itemRepository.findAll()).thenReturn(books);

        // when
        List<ItemEntity> bookList = itemService.getall();

        // then
        assertThat(bookList.size()).isEqualTo(5);
        assertThat(bookList.get(0).getName()).isEqualTo(books.get(0).getName());
        assertThat(bookList.get(1).getName()).isEqualTo(books.get(1).getName());
        assertThat(bookList.get(0).getPrice()).isEqualTo(books.get(0).getPrice());
        assertThat(bookList.get(1).getPrice()).isEqualTo(books.get(1).getPrice());

    }

    @Test
    @DisplayName("데이터가 존재 하지 않을때")
    void getItemSuccess2() {
        // stub

        // when

        // then
        assertThatThrownBy(() -> itemService.getall())
                .isInstanceOf(DataNotFoundException.class)
                .hasMessage(ErrorCode.DATA_DOSE_NOT_EXIST.getErrorMessage()); // Exception 객체가 가지고있는 메시지 검증


    }



}
