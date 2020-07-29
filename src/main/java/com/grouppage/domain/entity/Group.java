package com.grouppage.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "group_table")
@AllArgsConstructor
@NoArgsConstructor
public class Group extends AbstractEntityDate{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String category;

    @NotNull
    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @Column(columnDefinition = "boolean default false")
    private boolean isPrivate;

    @ManyToOne(targetEntity = Reaction.class)
    private Reaction reaction;

    @Column(columnDefinition = "boolean default false")
    private boolean isAccept;

    @Column(columnDefinition = "boolean default false")
    private boolean isForm;

    @Nullable
    private String inviteCode;

    private int participantCount;

    private String imageId;

    private long creator_id;
}
