package com.grouppage.domain.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grouppage.domain.notmapped.Layout;

import javax.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LayoutConverter implements AttributeConverter<List<Layout>, String> {


    private ObjectMapper getMapper() {
        return new ObjectMapper();
    }


    @Override
    public String convertToDatabaseColumn(List<Layout> layout) {
        if(null == layout){
            return null;
        }
        try {
            return this.getMapper().writeValueAsString(layout);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Override
    public List<Layout> convertToEntityAttribute(String s) {
        if(null == s){
            return null;
        }
        try {
            return new ArrayList<>(Arrays.asList(this.getMapper().readValue(s, Layout[].class)));
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
