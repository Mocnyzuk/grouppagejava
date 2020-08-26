package com.grouppage.domain.response;

import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.SignUpForm;
import com.grouppage.domain.notmapped.GroupForm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestNewGroup {
    @NotNull
    @Size(min = 5)
    private String nickname;
    @NotNull
    @Size(min = 2)
    private String name;
    @NotNull
    @Size(min = 2)
    private String description;
    @NotNull
    @Size(min = 2)
    private String category;
    @NotNull
    @Size(min = 2)
    private String imagePath;
    @NotNull
    private boolean isAccept;
    @NotNull
    private boolean isPrivate;
    @NotNull
    private boolean isForm;
    @NotNull
    private GroupForm groupForm;
    @NotNull
    private long reactionId;

    public Group toGroup(){
        Group group = new Group();
        group.setName(this.name);
        group.setDescription(this.description);
        group.setCategory(this.category);
        group.setImageId(this.imagePath);
        group.setPrivate(this.isPrivate);
        group.setAccept(this.isPrivate || this.isAccept);
        group.setInviteCode(UUID.randomUUID().toString());
        group.setForm((groupForm != null));
        return group;
    }
    public SignUpForm toSignUpForm(){
        SignUpForm form = new SignUpForm();
        form.setForm(this.groupForm);
        return form;
    }


    public Participant toParticipant() {
        Participant participant = new Participant();
        participant.setNickname(this.nickname);
        return participant;
    }
}
