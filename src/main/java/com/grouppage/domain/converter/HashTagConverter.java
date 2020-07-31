package com.grouppage.domain.converter;

import com.grouppage.domain.notmapped.HashTag;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HashTagConverter implements AttributeConverter<List<HashTag>, String> {
    @Override
    public String convertToDatabaseColumn(List<HashTag> hashTagList) {
        return hashTagList == null ? null : hashTagList.stream()
                .map(HashTag::getHashTag)
                .collect(Collectors.joining(";"));
    }

    @Override
    public List<HashTag> convertToEntityAttribute(String s) {
        return s == null ? null : Arrays.stream(s.split(";"))
                .map(HashTag::new)
                .collect(Collectors.toList());
    }
}
