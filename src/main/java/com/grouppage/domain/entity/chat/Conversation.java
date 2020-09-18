package com.grouppage.domain.entity.chat;

import com.grouppage.domain.entity.AbstractEntityDate;
import com.grouppage.domain.entity.Participant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
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
    @ManyToMany(targetEntity = Participant.class, fetch = FetchType.LAZY)
    private List<Participant> participants = new ArrayList<>();

}
