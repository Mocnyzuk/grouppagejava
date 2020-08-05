package com.grouppage.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostedPost {
    private long groupId;
    private long participantId;
    private String content;
}
