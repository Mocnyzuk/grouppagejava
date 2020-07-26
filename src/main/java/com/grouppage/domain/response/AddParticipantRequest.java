package com.grouppage.domain.response;

import lombok.Data;

@Data
public class AddParticipantRequest {
    private long conversationId;
    private long participantId;
}
