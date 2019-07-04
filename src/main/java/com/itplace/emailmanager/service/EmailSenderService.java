package com.itplace.emailmanager.service;

import com.itplace.emailmanager.domain.Mail;
import com.itplace.emailmanager.domain.MailTask;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.concurrent.CompletableFuture;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EmailSenderService {
    @Autowired
    private MailService mailService;
    @Autowired
    private MailTaskService mailTaskService;

    @Async
    public CompletableFuture<Mail> sendMail(String toEmail, Mail mail) throws Exception {
        mailService.changeMailStatus(mail, Mail.STATUS.SENDING);

        MailTask currentTask = mail.getMailTask();
        try {
            Email email = new SimpleEmail();
            email.setHostName(mail.getSender().getSmtp());
            email.setSmtpPort(mail.getSender().getPort());
            email.setAuthenticator(new DefaultAuthenticator(mail.getSender().getEmail(), mail.getSender().getEmailPassword()));
            email.setSSLOnConnect(true);
            email.setFrom(mail.getSender().getEmail());
            email.setSubject(mail.getSubject());
            email.setMsg(mail.getMessage());
            email.addTo(toEmail);
            email.send();
            mailService.changeMailStatus(mail, Mail.STATUS.SENT);
            switch (currentTask.getPeriod()){
                case ONCE:
                    currentTask.setStatus(MailTask.STATUS.DONE);
                    break;
                case DAILY: {
                    currentTask.setStartTime(currentTask.getStartTime() + getPeriodLong(MailTask.PERIOD.DAILY));
                    currentTask.setStatus(MailTask.STATUS.IN_PROGRESS);
                    break;
                }
                case WEEKLY: {
                    currentTask.setStartTime(currentTask.getStartTime() + getPeriodLong(MailTask.PERIOD.WEEKLY));
                    currentTask.setStatus(MailTask.STATUS.IN_PROGRESS);
                    break;
                }
                case MONTHLY: {
                    currentTask.setStartTime(currentTask.getStartTime() + getPeriodLong(MailTask.PERIOD.MONTHLY));
                    currentTask.setStatus(MailTask.STATUS.IN_PROGRESS);
                    break;
                }
                case YEARLY: {
                    currentTask.setStartTime(currentTask.getStartTime() + getPeriodLong(MailTask.PERIOD.YEARLY));
                    currentTask.setStatus(MailTask.STATUS.IN_PROGRESS);
                    break;
                }
            }
        } catch (Exception e) {
            currentTask.setStartTime(System.currentTimeMillis());
            mailService.changeMailStatus(mail, Mail.STATUS.FAILED, e.getMessage());
        }
        mailTaskService.save(currentTask);
        return CompletableFuture.completedFuture(mail);
    }

    private long getPeriodLong(MailTask.PERIOD period) {
        long day = 24 * 60 * 60 * 1000;
        switch (period) {
            case DAILY: return day;
            case WEEKLY: return 7 * day;
            case MONTHLY: return YearMonth.now().lengthOfMonth() * day;
            case YEARLY: return 120000;
        }
        return 0;
    }
}
