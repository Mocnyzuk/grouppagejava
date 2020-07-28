package com.grouppage.domain.notmapped;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HashTag {
    private String hashTag;
    public String getHashTag(){
        return this.hashTag;
    }
}
