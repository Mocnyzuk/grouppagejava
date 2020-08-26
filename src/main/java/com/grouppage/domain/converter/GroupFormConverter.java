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
        if(groupForm == null){
            return null;
        }
        Collection<Pair<String, String>> values = groupForm.getPairs();
//        if(values.stream().allMatch(p -> p.getValue().isEmpty())){
//            throw new GroupFormException("SOmething went wrong during parsing GroupForm");
//        }
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
        if(s == null){
            return null;
        }
        Map<String, String> map = new HashMap<>();
        Arrays.stream(s.split(";")).forEach(q -> {
            String[] arr = q.split("=");
            Pair<String, String> pair = new Pair<>();
            pair.setKey(arr[0]);
            pair.setValue(arr.length == 2? arr[1]: "");
            map.put(pair.getKey(), pair.getValue());

        });
        return new GroupForm(map);
    }
}
