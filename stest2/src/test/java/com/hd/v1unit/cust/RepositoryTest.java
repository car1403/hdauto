package com.hd.v1unit.cust;

import com.hd.v1.common.entity.CustEntity;
import com.hd.v1.app.cust.repository.CustRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
@DisplayName(" Repository Tests ")
@ActiveProfiles("h2db")
class RepositoryTest {

    @Autowired
    CustRepository repository;




    @BeforeEach
    public void db_init() {

        // repository.deleteAll();
        // MySQL
//        entityManager
//              .createNativeQuery("ALTER TABLE item_db AUTO_INCREMENT = 1;") // auto increment 초기화
//              .executeUpdate();

        // H2DB
//        entityManager
//                .createNativeQuery("ALTER TABLE item_db ALTER COLUMN id RESTART WITH 1") // auto increment 초기화
//                .executeUpdate();
    }

    @Test
    @DisplayName(" 1. Insert Test ")
    @Order(1)
    public void save_test() { // 괄호안에 뭐 넣으면 안돼

        //given
        String id = "id01";
        String pwd = "pwd01";
        String name = "name01";

        //when
        CustEntity custEntity = repository.save(CustEntity.builder().id(id).pwd(pwd).name(name).build());

        //verify
        assertThat(id).isEqualTo(custEntity.getId());
        assertThat(pwd).isEqualTo(custEntity.getPwd());
        assertThat(name).isEqualTo(custEntity.getName());

    }

    @Test
    @Order(2)
    @DisplayName(" 2. FindById Test ")
    public void findById_test() {

        //given
        String inputid = "id01";
        String pwd = "pwd01";
        String name = "name01";
        //when
        repository.save(CustEntity.builder().id(inputid).pwd(pwd).name(name).build());
        Optional<CustEntity> cust = repository.findById(inputid);

        //then
        if (cust.isPresent()) {
            CustEntity custEntity = cust.get();
            assertThat(inputid).isEqualTo(custEntity.getId());
            assertThat(pwd).isEqualTo(custEntity.getPwd());
            assertThat(name).isEqualTo(custEntity.getName());
        } else {
            assertNotNull(cust.get());
        }
    }

    @Test
    @Order(3)
    @DisplayName(" 3. Find All ")
    public void findAll_test() {
        // given
        String id = "id01";
        String pwd = "pwd01";
        String name = "name01";


        // when
        CustEntity custEntity =  repository.save(CustEntity.builder().id(id).pwd(pwd).name(name).build());

        List<CustEntity> custs = repository.findAll();

        // then
        assertThat(custs.size()).isEqualTo(1);

        assertThat(custs.get(0).getId()).isEqualTo(id);
        assertThat(custs.get(0).getPwd()).isEqualTo(pwd);
        assertThat(custs.get(0).getName()).isEqualTo(name);

    }

    @Test
    @Order(4)
    @DisplayName(" 4. Update Test ")
    public void update_test() {

        //given
        String id = "id01";
        String pwd = "pwd01";
        String name = "name01";

        String upwd = "pwd11";
        String uname = "name11";
        //when
        repository.save(CustEntity.builder().id(id).name(name).pwd(pwd).build());
        CustEntity custEntity = repository.save(CustEntity.builder().id(id).name(uname).pwd(upwd).build());
        //then
        assertThat(id).isEqualTo(custEntity.getId());
        assertThat(upwd).isEqualTo(custEntity.getPwd());
        assertThat(uname).isEqualTo(custEntity.getName());

    }

    @Test
    @Order(5)
    @DisplayName(" 5. Delete Test ")
    public void delete_test() {

        //given
        String id = "id01";
        String pwd = "pwd01";
        String name = "name01";

        repository.save(CustEntity.builder().id(id).name(name).pwd(pwd).build());

        //when
        Optional<CustEntity> cust = repository.findById(id);
        repository.delete(cust.get());


        //then
        assertThat(repository.findById(id)).isNotPresent();

    }
}