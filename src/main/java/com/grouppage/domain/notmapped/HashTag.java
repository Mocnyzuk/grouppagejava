package com.grouppage.domain.notmapped;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HashTag {
    private String value;
    public String getHashTag(){
        return this.value;
    }
}
