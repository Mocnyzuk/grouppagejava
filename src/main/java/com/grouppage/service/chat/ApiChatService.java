package com.grouppage.service.chat;

import com.grouppage.domain.repository.ParticipantRepository;
import com.grouppage.domain.repository.chat.ConversationRepository;
import com.grouppage.domain.repository.chat.PrivateMessageRepository;
import com.grouppage.domain.response.PostedMessage;
import org.springframework.stereotype.Service;

@Service
public class ApiChatService {

    private final ParticipantRepository participantRepository;
    private final ConversationRepository conversationRepository;
    private final PrivateMessageRepository privateMessageRepository;

    public ApiChatService(ParticipantRepository participantRepository,
                          ConversationRepository conversationRepository,
                          PrivateMessageRepository privateMessageRepository) {
        this.participantRepository = participantRepository;
        this.conversationRepository = conversationRepository;
        this.privateMessageRepository = privateMessageRepository;
    }


    public void handleNewChat(PostedMessage postedMessage, String receiver) {
        // TODO implememnt fields in PostedMessage in addition to saving in DB
    }
}
