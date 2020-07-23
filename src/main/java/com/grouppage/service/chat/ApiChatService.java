package com.grouppage.service.chat;

import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.chat.Conversation;
import com.grouppage.domain.entity.chat.PrivateMessage;
import com.grouppage.domain.repository.ParticipantRepository;
import com.grouppage.domain.repository.chat.ConversationRepository;
import com.grouppage.domain.repository.chat.PrivateMessageRepository;
import com.grouppage.domain.response.PostedMessage;
import com.grouppage.exception.WrongCredentialsException;
import com.grouppage.exception.WrongDataPostedException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

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


    public void handleNewChat(PostedMessage postedMessage, String receiver) throws WrongDataPostedException {
        if(!receiver.isEmpty() && null != postedMessage){
            throw new WrongDataPostedException("Posted data is invalid!");
        }
        Conversation conversation = new Conversation();
        Participant first = participantRepository.findById(postedMessage.getSenderId())
                .orElseThrow(() -> new WrongDataPostedException("Posted data is invalid!"));
        Participant second = participantRepository.findById(Long.parseLong(receiver))
                .orElseThrow(() -> new WrongDataPostedException("Posted data is invalid!"));
        conversation.setParticipants(Arrays.asList(first, second));
        conversation = conversationRepository.save(conversation);
        PrivateMessage privateMessage = new PrivateMessage();
        privateMessage.setConversation(conversation);
        privateMessage.setContent(postedMessage.getContent());
        privateMessage.setSender(first);
        privateMessageRepository.save(privateMessage);
    }
}
