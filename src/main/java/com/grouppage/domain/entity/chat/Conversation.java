package com.grouppage.domain.entity.chat;

import com.grouppage.domain.entity.AbstractEntityDate;
import com.grouppage.domain.entity.Participant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Conversation extends AbstractEntityDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Nullable
    private String avatar;

    @Column(columnDefinition = "varchar(255) default 'Conversation'")
    @Nullable
    private String name;


    @Size(min = 1, max = 5)
    @ManyToMany(targetEntity = Participant.class)
    private List<Participant> participants;

}
