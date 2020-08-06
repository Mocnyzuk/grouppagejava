package com.grouppage.domain.notmapped;

import com.grouppage.domain.converter.Pair;
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


    public Collection<Pair<String, String>> getPairs(){
       return form.entrySet().stream()
                .map((q) -> new Pair<>(q.getKey(), q.getValue()))
                .collect(Collectors.toList());
    }

}
