package com.grouppage.domain.notmapped;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.SignUpForm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpFormLight {
    private long id;
    private long groupId;
    private String nickname;
    private GroupForm form;

    @JsonIgnore
    public static SignUpFormLight fromSignUpForm(SignUpForm signUpForm, long groupId){
        return new SignUpFormLight(signUpForm.getId(),
                groupId,
                signUpForm.getNickname(),
                signUpForm.getForm());
    }
    @JsonIgnore
    public SignUpForm toSignUpForm(Group group){
        return new SignUpForm(this.id, group, this.nickname, this.form);
    }
    @JsonIgnore
    public SignUpForm toSignUpForm(){
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setId(this.id);
        signUpForm.setNickname(this.nickname);
        signUpForm.setForm(this.form);
        return signUpForm;
    }
}
