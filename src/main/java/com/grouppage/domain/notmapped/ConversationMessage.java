package com.grouppage.domain.notmapped;

import com.grouppage.domain.entity.Participant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationMessage {
    @NotNull
    private long participantId;
    @NotNull
    private long id;
    @NotNull
    private List<ParticipantLight> content;
    @NotNull
    private Type type;
}
