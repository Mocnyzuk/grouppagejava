package com.grouppage.domain.notmapped;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocketMessage {
    @NotNull
    private long participantId;
    @NotNull
    private String content;
    @NotNull
    private Type type;

    public enum Type{
        CHAT, GROUP, IS_TYPING, NOTIFICATION, DISCONNECT, ERROR
    }
}
