package com.grouppage.domain.entity.chat;

import com.grouppage.domain.entity.AbstractEntityDate;
import com.grouppage.domain.entity.Participant;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class PrivateMessage extends AbstractEntityDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(columnDefinition = "varchar(255) default 'CHAT'")
    private String type;

    @NotNull
    private String content;

    @ManyToOne(targetEntity = Participant.class)
    private Participant sender;

    @ManyToOne(targetEntity = Conversation.class)
    private Conversation conversation;

}
