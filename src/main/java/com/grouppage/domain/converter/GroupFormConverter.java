package com.grouppage.domain.converter;

import com.grouppage.domain.notmapped.GroupForm;
import com.grouppage.exception.GroupFormException;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GroupFormConverter implements AttributeConverter<GroupForm, String> {
    @Override
    public String convertToDatabaseColumn(GroupForm groupForm) throws GroupFormException {
        Collection<Pair<String, String>> values = groupForm.getPairs();
        if(values.stream().noneMatch(p -> p.getValue().isEmpty())){
            throw new GroupFormException("SOmething went wrong during parsing GroupForm");
        }
        StringBuilder builder = new StringBuilder();
        values.forEach(p -> {
            builder
                    .append(p.getKey())
                    .append("=")
                    .append(p.getValue())
                    .append(";");
        });
        return builder.toString();
    }

    @Override
    public GroupForm convertToEntityAttribute(String s) throws GroupFormException{
        Map<String, String> map = new HashMap<>();
        Arrays.stream(s.split(";")).forEach(q -> {
            String[] arr = q.split("=");
            if(arr.length == 2){
                Pair<String, String> pair = new Pair<>();
                Arrays.stream(arr).forEach(w -> {
                    if (pair.getKey().isEmpty()) {
                        pair.setKey(w);
                    } else {
                        pair.setValue(w);
                    }
                });
                map.put(pair.getKey(), pair.getValue());
            }else {
                throw new GroupFormException("SOmethging went wrong when aprsing");
            }
        });
        return new GroupForm(map);
    }
}
