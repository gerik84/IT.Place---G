package com.itplace.emailmanager.service;

import com.itplace.emailmanager.domain.Mail;
import com.itplace.emailmanager.domain.MailLog;
import com.itplace.emailmanager.domain.MailTask;
import com.itplace.emailmanager.repositry.MailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailService extends BaseRepository<MailRepository, Mail> {
    @Autowired
    MailTaskService mailTaskService;
    @Autowired
    MailLogService mailLogService;

    /*
     * Не удаленные сообщения с временем отправки меньше текущего,
     * с незавершенной задачей и не стоящие в очереди на отправку
     */
    public List<Mail> findMailToSend() {
        return repository.findByWhenDeletedNullAndMailTask_StartTimeIsLessThanAndMailTask_StatusNotAndMailTask_StatusNotAndStatusNot
                (System.currentTimeMillis(), MailTask.STATUS.DONE, MailTask.STATUS.CANCELLED, Mail.STATUS.SENDING);
    }

    public Mail saveNewMail(Mail mail){
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

    public void changeStatus(Mail mail, Mail.STATUS status, String message){
        MailLog mailLog = new MailLog();
        mailLog.setMailStatus(status);
        mailLog.setMessage(message);
        mailLogService.save(mailLog);
        mail.getMailLog().add(mailLog);

        mail.setStatus(status);
        repository.save(mail);
    }

    public List<Mail> findBySubjectLike(String subject) {
        return repository.findBySubjectIgnoreCaseLike(subject);
    }

    public Mail findBySubject(String subject){
        return repository.findBySubjectEquals(subject);
    }

    public List<Mail> findByAddresseId(Long id) {
        return repository.findByAddresseeId(id);
    }
  
    public List<Mail> findByAll(Long senderId, Integer page, Integer page_size, String sort, String direction) {
        return repository.findBySenderId(senderId, createPageable(page, page_size, sort, direction));
    }

    @Override
    public Mail save(Mail model) {
        return saveNewMail(model);
    }
}
