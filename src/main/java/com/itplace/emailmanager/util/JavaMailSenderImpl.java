package com.itplace.emailmanager.util;

import com.itplace.emailmanager.domain.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class JavaMailSenderImpl {
    @Autowired
    public JavaMailSender emailSender;

    public void sendMail(Mail mail) {
        // TODO сделать отправку почты из БД
    }

    public void sendScheduledMail(Mail mail, String timeDate) {
        // TODO сделать планировщик
    }
}
