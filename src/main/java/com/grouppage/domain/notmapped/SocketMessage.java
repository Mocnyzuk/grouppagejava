package com.grouppage.domain.notmapped;

import lombok.Data;

@Data
public class SocketMessage {

    private long participantId;
    private String content;
    private long destinationId;
    private Type type;

    public enum Type{
        CHAT, GROUP, IS_TYPING, NOTIFICATION
    }
}
