package com.grouppage.domain.response;

import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.SignUpForm;
import com.grouppage.domain.notmapped.GroupForm;
import com.grouppage.domain.notmapped.GroupLight;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InviteParticipant {
    @NotNull
    private GroupForm groupForm;

    public Participant getParticipant(){
        Participant participant = new Participant();
        participant.setNickname(this.groupForm.getForm().get("nickname"));
        participant.setEnabled(true);
        return participant;
    }
    public SignUpForm getSignUpForm(){
        String nickname = this.groupForm.getForm().get("nickname");
        this.groupForm.getForm().remove("nickname");
        SignUpForm form = new SignUpForm();
        form.setNickname(nickname);
        form.setForm(this.groupForm);
        return form;
    }
}
