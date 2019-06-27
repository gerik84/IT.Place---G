package com.itplace.emailmanager.util;

import com.itplace.emailmanager.domain.Mail;
import com.itplace.emailmanager.domain.MailTask;
import com.itplace.emailmanager.domain.Sender;
import com.itplace.emailmanager.service.MailService;
import com.itplace.emailmanager.service.MailTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

//@Component
public class EmailScheduler {
    @Autowired
    private MailService mailService;
    @Autowired
    private MailTaskService mailTaskService;

    @Scheduled(fixedRate = 5000)
    public void emailScheduler(){
        List<Mail> mailToSend = mailService.findMailToSend();
        if (mailToSend.size() > 0) {
            ExecutorService executorService = Executors.newCachedThreadPool();
            Set<Sender> senderSet = new HashSet<>();

            mailToSend.forEach(m -> senderSet.add(m.getSender()));
            for (Sender s : senderSet) {
                EmailService emailService = new EmailService(s);
                executorService.submit(() -> emailService.sendMail(mailToSend.stream().filter(m -> m.getSender().equals(s)).collect(Collectors.toList())));
            }
        }

        List<MailTask> mailTaskList = mailTaskService.findTasks();
        if (mailTaskList.size() > 0) {
            ExecutorService executorService = Executors.newCachedThreadPool();
            Set<Sender> senderSet = new HashSet<>();
            mailTaskList.forEach(t -> senderSet.add(t.getMail().getSender()));
            for (Sender s : senderSet) {
                EmailService emailService = new EmailService(s);
                List<Mail> mailList = new ArrayList<>();
                for (MailTask t: mailTaskList) {
                    if (t.getMail().getSender().equals(s)) {
                        mailList.add(t.getMail());
                        if (t.getRepeatsLeft() == 1) {
                            t.setRepeatsLeft(0);
                            t.setDone();
                        }
                        else {
                            t.setRepeatsLeft(t.getRepeatsLeft() - 1);
                            t.setStartTime(t.getStartTime() + t.getIntervalTime());
                        }
                    }
                }
                executorService.submit(() -> emailService.sendMail(mailList));
            }
        }
    }
}
