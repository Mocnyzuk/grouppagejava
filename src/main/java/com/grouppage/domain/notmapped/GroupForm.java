package com.grouppage.domain.notmapped;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grouppage.domain.converter.Pair;
import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.SignUpForm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupForm {
    @NotNull
    private Map<String, String> form;

    @JsonIgnore
    public Collection<Pair<String, String>> getPairs(){
       return form.entrySet().stream()
                .map((q) -> new Pair<>(q.getKey(), q.getValue()))
                .collect(Collectors.toList());
    }

    @JsonIgnore
    public Participant getParticipant() {
        Participant participant = new Participant();
        participant.setNickname(this.form.get("nickname"));
        participant.setEnabled(true);
        return participant;
    }

    @JsonIgnore
    public SignUpForm getSignUpForm() {
        String nickname = this.form.get("nickname");
        this.form.remove("nickname");
        SignUpForm form = new SignUpForm();
        form.setNickname(nickname);
        form.setForm(this);
        return form;
    }
}
