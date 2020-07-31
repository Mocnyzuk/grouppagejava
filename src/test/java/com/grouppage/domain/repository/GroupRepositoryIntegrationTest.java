package com.grouppage.domain.repository;

import com.grouppage.domain.entity.Group;
import com.grouppage.exception.GroupNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GroupRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GroupRepository groupRepository;

    @Test
    void savingInDB(){
        Group group = new Group();
        group.setName("testGroup");
        entityManager.persist(group);
        Group fromDB = this.groupRepository.findByName("testGroup").orElseThrow(
                () -> new GroupNotFoundException("NOT FOUND")
        );
        assertEquals(group.getName(), fromDB.getName());
    }
    @Test
    void proceedGroupSearchTest(){
        fail();
    }

    private void persistDataForProceedGroupSearchTest(){

    }
}