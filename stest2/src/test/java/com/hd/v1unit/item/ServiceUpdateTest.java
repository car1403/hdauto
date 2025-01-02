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
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

//@SpringBootTest

@Slf4j
@TestMethodOrder(MethodOrderer.DisplayName.class)
@DisplayName(" Service Update Test ")
@ExtendWith(MockitoExtension.class)
class ServiceUpdateTest {
    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    ItemService itemService;


    private long id;
    private String name;
    private long price;

    String newName;
    long newPrice;

    @BeforeEach
    void setup() {
        id = 1;
        name = "pants1";
        price = 50;
        newName = "pants2";
        newPrice = 100;
    }

    @Test
    @DisplayName("기존의 게시물 본문 내용 수정")
    void success1() {
        //given
        ItemEntity itemEntity = ItemEntity.builder().id(id).name(name).price(price).build();
        ItemEntity newItemEntity = ItemEntity.builder().id(id).name(newName).price(newPrice).build();

        //stub
        when(itemRepository.findById(id)).thenReturn(Optional.of(itemEntity));
        when(itemRepository.save(any(ItemEntity.class))).thenReturn(newItemEntity);

        // when
        ItemEntity result = itemService.modify(newItemEntity);

        //verify
        assertThat(result.getName()).isNotEqualTo(name);
        assertThat(result.getPrice()).isNotEqualTo(price);
        assertThat(result.getName()).isEqualTo(newName);
        assertThat(result.getPrice()).isEqualTo(newPrice);
    }

    @Test
    @DisplayName("아이디에 해당되는 게시물이 없는 경우")
    void fail1() {
        //given
        Long undefinedId = 200L;
        ItemEntity itemEntity = ItemEntity.builder().id(undefinedId).name(name).price(price).build();

        //stub
//                when(itemRepository.findById(undefinedId)).thenThrow(
//                        new IllegalArgumentException("id를 찾을 수 없습니다."));

        //when

        //verify
        assertThatThrownBy(() -> itemService.modify(itemEntity))
                .isInstanceOf(IdNotFoundException.class)
                .hasMessage(ErrorCode.ID_NOT_FOUND.getErrorMessage()); // Exception 객체가 가지고있는 메시지 검증

    }

}
