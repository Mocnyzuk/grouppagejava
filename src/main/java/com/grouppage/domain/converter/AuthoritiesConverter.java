package com.grouppage.domain.converter;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AuthoritiesConverter implements AttributeConverter<List<SimpleGrantedAuthority>, String> {
    @Override
    public String convertToDatabaseColumn(List<SimpleGrantedAuthority> simpleGrantedAuthorities) {
        return simpleGrantedAuthorities.stream()
                .map((s) -> s.getAuthority().toUpperCase())
                .collect(Collectors.joining(";"));
    }

    @Override
    public List<SimpleGrantedAuthority> convertToEntityAttribute(String s) {
        String[] roles = s.split(";");
        return Arrays.stream(roles)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
