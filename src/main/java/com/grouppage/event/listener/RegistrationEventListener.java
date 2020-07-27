package com.grouppage.event.listener;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.grouppage.event.RegistrationEvent;
import com.grouppage.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class RegistrationEventListener implements ApplicationListener<RegistrationEvent> {
    private final MailService mailService;

    @Autowired
    public RegistrationEventListener(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public void onApplicationEvent(RegistrationEvent registrationEvent) {
        this.mailService.sendMail(registrationEvent.getUser(), true);
    }
}
