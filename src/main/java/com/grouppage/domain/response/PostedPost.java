package com.grouppage.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostedPost {
    @NotNull
    private long groupId;
    @NotNull
    private long participantId;
    @NotNull
    private String content;
}
