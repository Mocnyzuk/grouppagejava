package com.grouppage.domain.notmapped;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationInfoWithMessages {
    @NotNull
    private long participantId;
    @NotNull
    private long id;
    @NotNull
    private List<ParticipantLight> participants;

    private List<SocketMessage> messages = new ArrayList<>();


}
