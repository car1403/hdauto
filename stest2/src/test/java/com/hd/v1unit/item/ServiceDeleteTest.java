package com.hd.v1unit.item;

import com.hd.common.exception.ErrorCode;
import com.hd.common.exception.IdNotFoundException;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hd.v1.app.item.repository.ItemRepository;
import com.hd.v1.app.item.service.ItemService;
import com.hd.v1.common.entity.ItemEntity;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

//@SpringBootTest

@Slf4j
@TestMethodOrder(MethodOrderer.DisplayName.class)
@DisplayName(" Item Service Delete Test ")
@ExtendWith(MockitoExtension.class)
class ServiceDeleteTest {
    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    ItemService itemService;

    private long id;
    private String name;
    private long price;


    @BeforeEach
    void setup() {
        id = 1;
        name = "pants1";
        price = 50;
    }

    @Test
    @DisplayName("특정 id 삭제")
    void success1() {
        // given
        ItemEntity itemEntity = ItemEntity.builder().id(id).name(name).price(price).build();

        //stub
        when(itemRepository.findById(id)).thenReturn(Optional.of(itemEntity));

        //when
        long resultId = itemService.remove(id);

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
        assertThatThrownBy(() -> itemService.remove(undefinedId))
                .isInstanceOf(IdNotFoundException.class)
                .hasMessage(ErrorCode.ID_NOT_FOUND.getErrorMessage()); // Exception 객체가 가지고있는 메시지 검증

    }



}
