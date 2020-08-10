package com.grouppage.domain.notmapped;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupLayout implements Serializable {
    public static final long serialVersionUID = 42123L;
    private String name;
    private int id;
    private int x;
    private int y;
    private int w;
    private int h;
}
