package com.itplace.emailmanager.service;

import com.itplace.emailmanager.domain.Mail;
import com.itplace.emailmanager.domain.MailTask;
import com.itplace.emailmanager.domain.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.Session;
import java.util.concurrent.CompletableFuture;

@Service
@Scope("prototype")
public class EmailSenderService {
    @Autowired
    private JavaMailSenderImpl emailSender;
    @Autowired
    private MailService mailService;
    @Autowired
    private MailTaskService mailTaskService;

    @Async
    public CompletableFuture<Mail> sendMail(String toEmail, Mail mail) throws InterruptedException {
        mailService.changeStatus(mail, Mail.STATUS.SENDING, null);

        setMailSenderProps(mail.getSender());
        MailTask currentTask = mail.getMailTask();
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(toEmail);
            mailMessage.setFrom(mail.getSender().getEmail());
            mailMessage.setSubject(mail.getSubject());
            mailMessage.setText(mail.getMessage());
            emailSender.send(mailMessage);
            mailService.changeStatus(mail, Mail.STATUS.SENT, null);
            // если задача не бесконечна
            if (currentTask.getRepeatsLeft() > 0) {
                currentTask.setRepeatsLeft(currentTask.getRepeatsLeft() - 1);
                currentTask.setStartTime(currentTask.getStartTime() + currentTask.getIntervalTime());
                currentTask.setStatus(MailTask.STATUS.IN_PROGRESS);
            }
            // если задача закончилась
            if (currentTask.getRepeatsLeft() == 0) currentTask.setStatus(MailTask.STATUS.DONE);
            // если задача бесконечна
            if (currentTask.getRepeatsLeft() == -1) {
                currentTask.setStartTime(currentTask.getStartTime() + currentTask.getIntervalTime());
                currentTask.setStatus(MailTask.STATUS.IN_PROGRESS);
            }
        } catch (MailException e) {
            // даем письму 5 попыток
            if (mail.getAttempts() < 5) {
                mail.setAttempts(mail.getAttempts() + 1);
                currentTask.setStatus(MailTask.STATUS.IN_PROGRESS);
                mailService.changeStatus(mail, Mail.STATUS.ERROR, e.getMessage());
            }
            else {
                currentTask.setStatus(MailTask.STATUS.CANCELLED);
                mailService.changeStatus(mail, Mail.STATUS.FAILED, e.getMessage());
            }
        }

        mailTaskService.save(currentTask);
        return CompletableFuture.completedFuture(mailService.findById(mail.getId()));
    }

    private void setMailSenderProps(Sender sender) {
        emailSender.setHost(sender.getSmtp());
        emailSender.setPort(sender.getPort());
        emailSender.setUsername(sender.getEmail());
        emailSender.setPassword(sender.getEmailPassword());
    }
}
