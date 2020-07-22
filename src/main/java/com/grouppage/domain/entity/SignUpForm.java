package com.grouppage.domain.entity;

import com.grouppage.domain.converter.GroupFormConverter;
import com.grouppage.domain.notmapped.GroupForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class SignUpForm extends AbstractEntityDate{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(targetEntity = Group.class)
    private Group group;

    @Column(columnDefinition = "varchar(255) default 'example'")
    private String nickname;

    @Convert(converter = GroupFormConverter.class)
    private GroupForm form;
}
