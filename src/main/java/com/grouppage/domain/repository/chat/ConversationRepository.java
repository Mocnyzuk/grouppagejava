package com.grouppage.domain.repository.chat;

import com.grouppage.domain.entity.chat.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}
