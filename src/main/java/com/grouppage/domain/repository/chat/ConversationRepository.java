package com.grouppage.domain.repository.chat;

import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.User;
import com.grouppage.domain.entity.chat.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findAllByParticipantsIn(Iterable<Participant> participants);

    @Query(value = "select c from Conversation c join fetch c.participants p join fetch p.user")
    List<Conversation> findAllByFetchParticipants();
}
