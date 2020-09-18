package com.grouppage.domain.notmapped;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grouppage.domain.entity.chat.PrivateMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivateMessageForGet {
    @NotNull
    private long participantId;
    @NotNull
    private long id;
    @NotNull
    private String content;
    @NotNull
    private List<ParticipantLight> participants;
    @NotNull
    private Type type;

    @JsonIgnore
    public static PrivateMessageForGet fromPrivateMessage(PrivateMessage privateMessage) {
        return new PrivateMessageForGet(privateMessage.getSender().getId(),
                privateMessage.getConversation().getId(),
                privateMessage.getContent(),
                privateMessage.getConversation().getParticipants().stream().map(ParticipantLight::fromParticipant).collect(Collectors.toList()),
                Type.valueOf(privateMessage.getType()));
    }
}
