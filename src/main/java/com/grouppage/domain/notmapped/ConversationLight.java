package com.grouppage.domain.notmapped;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grouppage.domain.entity.chat.Conversation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationLight{
        private long id;
        private String avatar;
        private String name;
        private List<ParticipantLight> participants = new ArrayList<>();

        @JsonIgnore
        public static ConversationLight fromConversation(Conversation conversation){
                return new ConversationLight(conversation.getId(),
                        conversation.getAvatar(),
                        conversation.getName(),
                        conversation.getParticipants().stream().map(ParticipantLight::fromParticipant).collect(Collectors.toList()));
        }
}
