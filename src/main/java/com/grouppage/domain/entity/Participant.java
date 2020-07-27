package com.grouppage.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class Participant extends AbstractEntityDate{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String nickname;

    @OneToMany(targetEntity = Post.class)
    @JsonIgnoreProperties("author")
    private List<Post> likedPosts = new ArrayList<>();

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @OneToOne(targetEntity = Group.class)
    private Group group;

    private boolean isEnabled = false;
}
