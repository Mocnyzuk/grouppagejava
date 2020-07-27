package com.grouppage.service;

import com.grouppage.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender mailSender;
    private final String email;

    @Autowired
    public MailService(
            JavaMailSender mailSender,
            @Value("${spring.mail.username}")String email) {
        this.mailSender = mailSender;
        this.email = email;
    }
    public void sendMail(User user, boolean activation){
        if(activation)
            this.mailSender.send(this.constructAccountActivationLink(user));
    }
    private SimpleMailMessage constructAccountActivationLink(User user){
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(this.email);
        mail.setSubject("Thanks for registering click the activation link!");
        mail.setText("Hello "+ user.getEmail()+ "\n"+
                "<a href=\"www.nazwastrony.pl/activate?id="+user.getResetPasswordToken()+"\">click</a>" );
        mail.setTo(user.getEmail());
        return mail;
    }
}
