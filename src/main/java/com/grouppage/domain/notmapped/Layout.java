package com.grouppage.domain.notmapped;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Layout implements Serializable {
    public static final long serialVersionUID = 42123L;
    private String name;
    private List<GroupLayout> groups;
}
