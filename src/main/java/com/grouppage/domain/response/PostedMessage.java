package com.grouppage.domain.response;

import lombok.Data;

@Data
public class PostedMessage {

    private long senderId;
    private String content;
}
