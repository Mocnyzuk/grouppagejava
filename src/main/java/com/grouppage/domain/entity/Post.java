package com.grouppage.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.grouppage.domain.converter.HashTagConverter;
import com.grouppage.domain.notmapped.HashTag;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Post extends AbstractEntityDate{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(targetEntity = Group.class)
    private Group group;

    @OneToOne(targetEntity = Participant.class)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    @JsonIgnoreProperties("likedPosts")
    private Participant author;

    // TODO remove SQL injection posiibility
    @NotNull
    //@Pattern(regexp = "")
    private String content;

    @Convert(converter = HashTagConverter.class)
    @JsonIgnore
    private List<HashTag> hashTags;

    @Column(columnDefinition = "integer default 0")
    private int reactionCount;
}
