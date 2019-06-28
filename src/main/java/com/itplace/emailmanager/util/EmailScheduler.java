package com.itplace.emailmanager.util;

import com.itplace.emailmanager.domain.Mail;
import com.itplace.emailmanager.domain.MailTask;
import com.itplace.emailmanager.service.MailService;
import com.itplace.emailmanager.service.MailTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EmailScheduler {
    @Autowired
    EmailService emailService;
    @Autowired
    private MailService mailService;
    @Autowired
    private MailTaskService mailTaskService;

    @Scheduled(fixedRate = 5000)
    public void emailScheduler(){
        List<Mail> mailToSend = mailService.findMailsToSend();
        if (mailToSend.size() > 0) {
            emailService.sendMail(mailToSend);
        }

        List<Mail> taskedMail = new ArrayList<>();
        List<MailTask> mailTaskList = mailTaskService.findTasksToDo();
        if (mailTaskList.size() > 0) {
            for (MailTask t: mailTaskList) {
                if (t.getRepeatsLeft() == 0) {
                    t.setDone();
                    mailTaskService.save(t);
                }
                else {
                    t.setRepeatsLeft(t.getRepeatsLeft() - 1);
                    t.setStartTime(t.getStartTime() + t.getIntervalTime());
                    mailTaskService.save(t);
                    taskedMail.add(t.getMail());
                }
            }
            emailService.sendMail(taskedMail);
        }
    }
}
