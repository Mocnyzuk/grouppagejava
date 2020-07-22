package com.grouppage.domain.repository.chat;

import com.grouppage.domain.entity.chat.PrivateMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Long> {
}
