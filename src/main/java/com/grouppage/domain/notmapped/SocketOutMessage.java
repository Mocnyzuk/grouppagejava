package com.grouppage.domain.notmapped;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocketOutMessage {
    @NotNull
    private long participantId;
    @NotNull
    private long id;
    @NotNull
    private String content;
    @NotNull
    private Type type;
}
