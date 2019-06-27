package com.itplace.emailmanager.util;

import com.itplace.emailmanager.domain.Mail;
import com.itplace.emailmanager.domain.Sender;
import com.itplace.emailmanager.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class EmailService {
    @Autowired
    private JavaMailSenderImpl emailSender;
    @Autowired
    private MailService mailService;

    public void sendMail(Mail mail) {
        setMailSenderProps(mail.getSender());
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            ArrayList<String> toList = new ArrayList<>();
            mail.getAddressee().forEach(a -> toList.add(a.getEmail()));
            mailMessage.setTo(toList.toArray(new String[0]));
            mailMessage.setFrom(mail.getSender().getEmail());
            mailMessage.setSubject(mail.getSubject());
            mailMessage.setText(mail.getMessage());

            emailSender.send(mailMessage);
        } catch (MailException e) {
            sendScheduledMail(mail, false, null, System.currentTimeMillis() + 60000);
        }

        mail.setDelivered();
        mailService.save(mail);
    }

    public void sendScheduledMail (Mail mail, boolean isRegular, @Nullable Long interval, @Nullable Long timeDate) {
        setMailSenderProps(mail.getSender());

    }

    private void setMailSenderProps(Sender sender){
        emailSender.setHost(sender.getSmtp());
        emailSender.setPort(sender.getPort());
        emailSender.setUsername(sender.getEmail());
        emailSender.setPassword(sender.getPassword());
    }
}
