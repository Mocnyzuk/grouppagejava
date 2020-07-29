package com.grouppage.domain.notmapped;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocketMessage {

    private long participantId;
    private String content;
    private Type type;

    public enum Type{
        CHAT, GROUP, IS_TYPING, NOTIFICATION, DISCONNECT
    }
}
