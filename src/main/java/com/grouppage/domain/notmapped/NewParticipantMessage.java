package com.grouppage.domain.notmapped;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewParticipantMessage {
    @NotNull
    private long participantId;
    @NotNull
    private long id;
    @NotNull
    private String nickname;
    @NotNull
    private Type type;
}
