package com.grouppage.domain.notmapped;

import com.grouppage.domain.entity.Group;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupLight {
    private long id;
    private String category;
    private String name;
    private String description;
    private boolean isPrivate;
    private boolean isAccept;
    private boolean isForm;
    private String inviteCode;
    private int participantCount;
    private String imageId;
    private long creatorId;

    public static GroupLight fromGroup(Group group){
        return new GroupLight(group.getId(), group.getCategory(),
                group.getName(), group.getDescription(),
                group.isPrivate(), group.isAccept(), group.isForm(),
                group.getInviteCode(), group.getParticipantCount(),
                group.getImageId(), group.getCreatorId());
    }
}
