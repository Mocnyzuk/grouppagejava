package com.grouppage.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"nickname" , "group_id", "user_id"})})
public class Participant extends AbstractEntityDate{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String nickname;

    @OneToOne(targetEntity = User.class)
    private User user;

    @OneToOne(targetEntity = Group.class)
    private Group group;

    private boolean isEnabled = false;
}
