package com.grouppage.domain.response;

import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.SignUpForm;
import com.grouppage.domain.notmapped.GroupForm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestNewGroup {
    private String nickname;

    private String name;
    private String description;
    private String category;
    private String imagePath;

    private boolean isAccept;
    private boolean isPrivate;
    private boolean isForm;

    private GroupForm groupForm;


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
