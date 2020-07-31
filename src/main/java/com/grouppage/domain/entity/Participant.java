package com.grouppage.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"nickname" , "group_id", "user_id"})})
@AllArgsConstructor
@NoArgsConstructor
public class Participant extends AbstractEntityDate{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String nickname;

    @ManyToMany(targetEntity = Post.class, fetch = FetchType.LAZY)
    //@JsonIgnoreProperties("author")
    private List<Post> likedPosts = new ArrayList<>();

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(targetEntity = Group.class, fetch = FetchType.LAZY)
    private Group group;

    private boolean isEnabled = false;
}
