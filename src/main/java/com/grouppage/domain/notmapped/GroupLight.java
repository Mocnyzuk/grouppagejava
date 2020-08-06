package com.grouppage.domain.notmapped;

import com.grouppage.domain.entity.Group;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupLight {
    @NotNull
    private long id;
    @NotNull
    private String category;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private boolean isPrivate;
    @NotNull
    private boolean isAccept;
    @NotNull
    private boolean isForm;
    @NotNull
    private String inviteCode;
    @NotNull
    private int participantCount;
    @NotNull
    private String imageId;
    @NotNull
    private long creatorId;

    public static GroupLight fromGroup(Group group){
        return new GroupLight(group.getId(), group.getCategory(),
                group.getName(), group.getDescription(),
                group.isPrivate(), group.isAccept(), group.isForm(),
                group.getInviteCode(), group.getParticipantCount(),
                group.getImageId(), group.getCreatorId());
    }
    public static Group fromGroupLight(GroupLight group){
        Group result = new Group();
        result.setId(group.getId());
        result.setCategory(group.getCategory());
        result.setDescription(group.getDescription());
        result.setPrivate(group.isPrivate());
        result.setName(group.getName());
        result.setAccept(group.isAccept());
        result.setForm(group.isForm());
        result.setInviteCode(group.getInviteCode());
        result.setParticipantCount(group.getParticipantCount());
        result.setImageId(group.getImageId());
        result.setCreatorId(group.getCreatorId());
        return result;
    }
}
