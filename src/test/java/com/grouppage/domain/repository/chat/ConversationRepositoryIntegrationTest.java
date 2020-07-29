package com.grouppage.domain.repository.chat;

import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.chat.Conversation;
import com.grouppage.domain.repository.GroupRepository;
import com.grouppage.domain.repository.ParticipantRepository;
import com.grouppage.exception.ConversationNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ConversationRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Test
    void testSavingEntity(){
        Conversation conversation = new Conversation();
        List<Participant> participantList = this.participantRepository.findAll().subList(0, 2);
        conversation.setParticipants(participantList);
        Conversation fromEM = entityManager.persist(conversation);

        Conversation fromDB = this.conversationRepository.findById(fromEM.getId()).orElseThrow(
                () -> new ConversationNotFoundException("NOT FOUND")
        );
        assertEquals(conversation.getParticipants().size(), fromDB.getParticipants().size());
    }

}