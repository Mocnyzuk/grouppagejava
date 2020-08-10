package com.grouppage.domain.notmapped;

import com.grouppage.domain.entity.Participant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantLight {
    private long id;
    private String nickname;

    public static ParticipantLight fromParticipant(Participant participant){
        return new ParticipantLight(participant.getId(),
                participant.getNickname());
    }

}