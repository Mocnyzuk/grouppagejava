package com.grouppage.domain.notmapped;

import com.grouppage.domain.entity.chat.PrivateMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Socket;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocketMessage {
    @NotNull
    private long participantId;
    @NotNull
    private String content;
    @NotNull
    private Type type;

    public static SocketMessage fromPrivateMessage(PrivateMessage privateMessage) {
        return new SocketMessage(privateMessage.getSender().getId(),
                privateMessage.getContent(),
                Type.valueOf(privateMessage.getType()));
    }
}
