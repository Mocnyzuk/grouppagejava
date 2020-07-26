package com.grouppage.domain.repository.chat;

import com.grouppage.domain.entity.chat.Conversation;
import com.grouppage.domain.entity.chat.PrivateMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Long> {
    List<PrivateMessage> findAllByConversation(Conversation conversation);
}
