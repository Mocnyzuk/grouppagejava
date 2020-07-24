package com.grouppage.domain.repository.chat;

import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.chat.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findAllByParticipantsIn(Iterable<Participant> participants);
}
