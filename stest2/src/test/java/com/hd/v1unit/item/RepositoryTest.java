package com.hd.v1unit.item;

import com.hd.v1.common.entity.ItemEntity;
import com.hd.v1.app.item.repository.ItemRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

//@SpringBootTest

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 메모리 DB 사용 안함
@Import(value = com.hd.config.JpaAuditingConfig.class) // JPA Auditing
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName(" Repository Tests ")
@ActiveProfiles("dev")
class RepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    public void db_init() {

        //itemRepository.deleteAll();
        // MySQL
        entityManager
                .createNativeQuery("ALTER TABLE db_item AUTO_INCREMENT = 1;") // auto increment 초기화
                .executeUpdate();

//         H2DB
//        entityManager
//                .createNativeQuery("ALTER TABLE db_item ALTER COLUMN id RESTART WITH 1") // auto increment 초기화
//                .executeUpdate();
    }

    @Test
    @DisplayName(" 1. Insert Test ")
    @Order(1)
    public void save_test() { // 괄호안에 뭐 넣으면 안돼

        //given
        String name = "pants2";
        long price = 1000L;

        //when
        ItemEntity itemEntity = itemRepository.save(ItemEntity.builder().name(name).price(price).build());
        //verify
        assertThat(name).isEqualTo( itemEntity.getName());
        assertEquals(price, itemEntity.getPrice());
        log.info( itemEntity.getId()+"---------------------");
        assertEquals(1, itemEntity.getId());
    }

    @Test
    @Order(2)
    @DisplayName(" 2. FindById Test ")
    public void findById_test() {

        //given
        String name = "pants2";
        long price = 1000L;
        itemRepository.save(ItemEntity.builder().name(name).price(price).build());

        Long id = 1L; //long 타입은 뒤에 l을 붙인다

        //when
        Optional<ItemEntity> item = itemRepository.findById(id);
        log.info("itemEntity: {}", item.get());

        //then
        if (item.isPresent()) {
            ItemEntity itemEntity = item.get();
            assertEquals(name, itemEntity.getName());
            assertEquals(price, itemEntity.getPrice());
            log.info( itemEntity.getId()+"");

            assertEquals(1, itemEntity.getId());
        } else {
            assertNotNull(item.get());
        }
    }

    @Test
    @Order(3)
    @DisplayName(" 3. Find All ")
    public void findAll_test() {
        // given
        String name = "pants2";
        long price = 1000L;
        ItemEntity item = itemRepository.save(ItemEntity.builder().name(name).price(price).build());
        ItemEntity item2 = itemRepository.save(ItemEntity.builder().name(name+"2").price(price).build());

        // when
        List<ItemEntity> items = itemRepository.findAll();
        log.info("Size-----------------------------------------------------------------"+items.size());
        // then
        assertThat(item.getPrice()).isEqualTo(items.get(0).getPrice());
        assertThat(item.getName()).isEqualTo(items.get(0).getName());

    }

    @Test
    @Order(4)
    @DisplayName(" 4. Update Test ")
    public void update_test() {

        //given
        String name = "pants2";
        long price = 1000L;
        ItemEntity item1 = itemRepository.save(ItemEntity.builder().name(name).price(price).build());

        //when
        ItemEntity item = itemRepository.save(ItemEntity.builder().id(1L).name("pants3").price(2000L).build());

        //then
        assertThat(item.getPrice()).isEqualTo(2000L);
        assertThat(item.getName()).isEqualTo("pants3");

    }

    @Test
    @Order(5)
    @DisplayName(" 5. Delete Test ")
    public void delete_test() {

        //given
        String name = "pants";
        long price = 1000L;
        ItemEntity item = itemRepository.save(ItemEntity.builder().name(name).price(price).build());
        Long id = 1L;

        //when
        Optional<ItemEntity> item2 = itemRepository.findById(id);
        itemRepository.delete(item2.get());


        //then
        assertThat(itemRepository.findById(id)).isNotPresent();

    }
}