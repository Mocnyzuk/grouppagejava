package com.grouppage.domain.repository.chat;

import com.grouppage.domain.entity.chat.Conversation;
import com.grouppage.domain.entity.chat.PrivateMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Long> {
    List<PrivateMessage> findAllByConversation(Conversation conversation);

    @Query(value = "select p from PrivateMessage p join fetch p.sender where p.conversation.id = :conversationId")
    List<PrivateMessage> findAllByConversationId(@Param("conversationId")long conversationId);
}
