package com.itplace.emailmanager.util;

import com.itplace.emailmanager.domain.Mail;
import com.itplace.emailmanager.domain.Sender;
import com.itplace.emailmanager.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EmailService {
    @Autowired
    private JavaMailSenderImpl emailSender;
    @Autowired
    private MailService mailService;

    public void sendMail(List<Mail> mail) {
        for (Mail m: mail) {
            setMailSenderProps(m.getSender());
            try {
                SimpleMailMessage mailMessage = new SimpleMailMessage();

                ArrayList<String> toList = new ArrayList<>();
                m.getAddressee().forEach(a -> toList.add(a.getEmail()));
                mailMessage.setTo(toList.toArray(new String[0]));
                mailMessage.setFrom(m.getSender().getEmail());
                mailMessage.setSubject(m.getSubject());
                mailMessage.setText(m.getMessage());

                emailSender.send(mailMessage);
            } catch (MailException e) {
                System.out.println(e.getMessage());
                if (m.getAttempts() < 5) m.setAttempts(m.getAttempts() + 1);
                else m.setStatus(Mail.STATUS.FAILED);
                mailService.save(m);
                continue;
            }
            m.setSent();
            mailService.save(m);
        }
    }

    private void setMailSenderProps(Sender sender){
        emailSender.setHost(sender.getSmtp());
        emailSender.setPort(sender.getPort());
        emailSender.setUsername(sender.getEmail());
        emailSender.setPassword(sender.getPassword());
    }
}
