package com.hd.v1unit.item;

import com.hd.common.exception.ErrorCode;
import com.hd.common.exception.IdNotFoundException;
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
//@SpringBootTest

@Slf4j
@TestMethodOrder(MethodOrderer.DisplayName.class)
@DisplayName(" Service Get Test ")
@ExtendWith(MockitoExtension.class)
class ServiceGetTest {
    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    ItemService itemService;

    private long id;
    private String name;
    private long price;

    @BeforeEach
    void setup() {
        id = 10L;
        name = "pants1";
        price = 50;
    }

    @Test
    @DisplayName("정상 케이스")
    void success1() {
        // give
        ItemEntity resultEntity = ItemEntity.builder().id(1L).name(name).price(price).build();
        long itemId = 1L;
        //stub
        when(itemRepository.findById(any())).thenReturn(Optional.of(resultEntity));

        // when

        ItemEntity result = itemService.get(itemId);

        //  verify
        assertThat(result.getId()).isEqualTo(resultEntity.getId());
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getPrice()).isEqualTo(price);
    }
    @Test
    @DisplayName("특정 id가 존재하지 않을때")
    void fail1() {
        // give
        Long undefinedId = 200L;

        // when

        //  verify

        assertThatThrownBy(() -> itemService.get(undefinedId))
                .isInstanceOf(IdNotFoundException.class)
                .hasMessage(ErrorCode.ID_NOT_FOUND.getErrorMessage()); // Exception 객체가 가지고있는 메시지 검증

    }



}
