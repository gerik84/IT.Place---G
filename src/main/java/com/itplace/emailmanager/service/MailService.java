package com.itplace.emailmanager.service;

import com.itplace.emailmanager.domain.Addressee;
import com.itplace.emailmanager.domain.Mail;
import com.itplace.emailmanager.domain.MailTask;
import com.itplace.emailmanager.repositry.MailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MailService extends BaseRepository<MailRepository, Mail> {
    @Autowired
    MailTaskService mailTaskService;
    @Autowired
    SenderService senderService;

    public List<Mail> findBySubjectLike(String subject) {
        return repository.findBySubjectIgnoreCaseLike(subject);
    }

    public List<Mail> findMailToSend() {
        /*return repository.findByWhenDeletedNotNullAndMailTask_StartTimeBeforeAndMailTask_RepeatsLeftNotAndMailTask_StatusNotAndStatusNot
                (System.currentTimeMillis(), 0, MailTask.STATUS.DONE, Mail.STATUS.SENDING);*/

        List<Mail> mailList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Mail mail = new Mail();
            List<Addressee> addresseeList = new ArrayList<>();
            Addressee addressee1 = new Addressee();
            addressee1.setEmail("1.2@3.com");
            Addressee addressee2 = new Addressee();
            addressee2.setEmail("4.5@6.com");
            mail.setAddressee(addresseeList);
            mailList.add(mail);
        }
        return mailList;
    }

    public Mail findBySubject(String subject){
        return repository.findBySubjectEquals(subject);
    }

    public List<Mail> findByAddresseId(Long id) {
        return repository.findByAddresseeId(id);
    }

    public Mail saveMail(Mail mail){
        MailTask mailTask;
        if (mail.getMailTask() == null) {
            mailTask = new MailTask();
            mailTask.setStartTime(System.currentTimeMillis());
            mailTask.setRepeatsLeft(1);
            mailTask.setIntervalTime(0);
            mailTaskService.save(mailTask);
        }
        else mailTaskService.save(mail.getMailTask());

        mailTask = mailTaskService.getLastAdded();
        mail.setMailTask(mailTask);
        return repository.save(mail);
    }


}
