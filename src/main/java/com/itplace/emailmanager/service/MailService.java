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

    /*
     * Не удаленные сообщения с временем отправки меньше текущего,
     * с незавершенной задачей и не стоящие в очереди на отправку
     */
    public List<Mail> findMailToSend() {
        return repository.findByWhenDeletedNotNullAndMailTask_StartTimeBeforeAndMailTask_StatusNotAndStatusNot
                (System.currentTimeMillis(), MailTask.STATUS.DONE, Mail.STATUS.SENDING);
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

    public void changeStatus(Mail mail, Mail.STATUS status){
        // TODO добавить логгирование
        mail.setStatus(status);
        repository.save(mail);
    }


}
