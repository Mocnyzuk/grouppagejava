package com.grouppage.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddParticipantRequest {
    private long conversationId;
    private long participantId;
}
