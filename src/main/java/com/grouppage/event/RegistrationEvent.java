package com.grouppage.event;

import com.grouppage.domain.entity.User;
import org.springframework.context.ApplicationEvent;

public class RegistrationEvent extends ApplicationEvent {
    private final User user;
    public RegistrationEvent(User source) {
        super(source);
        this.user = source;
    }

    public User getUser() {
        return user;
    }
}
