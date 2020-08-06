package com.grouppage.domain.response;

import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.SignUpForm;
import com.grouppage.domain.notmapped.GroupForm;
import com.grouppage.domain.notmapped.GroupLight;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InviteParticipant {
    @NotNull
    private GroupLight groupLight;
    @NotNull
    private String nickname;
    @NotNull
    private GroupForm groupForm;

    public Participant getParticipant(){
        Participant participant = new Participant();
        participant.setNickname(this.nickname);
        participant.setEnabled(true);
        return participant;
    }
    public SignUpForm getSignUpForm(){
        SignUpForm form = new SignUpForm();
        form.setForm(this.groupForm);
        form.setNickname(this.nickname);
        return form;
    }
}
