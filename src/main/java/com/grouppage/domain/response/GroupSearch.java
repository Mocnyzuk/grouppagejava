package com.grouppage.domain.response;

import com.grouppage.domain.entity.Group;
import com.grouppage.domain.notmapped.GroupLight;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupSearch {
    @NotNull
    private long id;
    @NotNull
    private String category;
    @NotNull
    private String name;
    @NotNull
    private String color;
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

    private boolean isMember;

    public static GroupSearch fromGroup(Group group, boolean isMember){
        return new GroupSearch(group.getId(), group.getCategory(),
                group.getName(), group.getColor(), group.getDescription(),
                group.isPrivate(), group.isAccept(), group.isForm(),
                group.getInviteCode(), group.getParticipantCount(),
                group.getImageId(), group.getCreatorId(), isMember);
    }

    public static Group fromGroupLight(GroupLight group){
        Group result = new Group();
        result.setId(group.getId());
        result.setCategory(group.getCategory());
        result.setDescription(group.getDescription());
        result.setPrivate(group.isPrivate());
        result.setName(group.getName());
        result.setColor(group.getColor());
        result.setAccept(group.isAccept());
        result.setForm(group.isForm());
        result.setInviteCode(group.getInviteCode());
        result.setParticipantCount(group.getParticipantCount());
        result.setImageId(group.getImageId());
        result.setCreatorId(group.getCreatorId());
        return result;
    }
}
