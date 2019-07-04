package com.itplace.emailmanager.service;

import com.itplace.emailmanager.domain.Mail;
import com.itplace.emailmanager.domain.MailLog;
import com.itplace.emailmanager.domain.MailTask;
import com.itplace.emailmanager.repositry.MailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public List<Mail> findMailToSend() {
        return repository.findByWhenDeletedNullAndMailTask_StartTimeIsLessThanAndMailTask_StatusNotAndMailTask_StatusNotAndStatusNot
                (System.currentTimeMillis(), MailTask.STATUS.PAUSED, MailTask.STATUS.DONE, Mail.STATUS.SENDING);
    }

    @Transactional
    public Mail createNewMail(Mail mail){
        MailTask mailTask;
        if (mail.getMailTask() == null) {
            mailTask = new MailTask();
            mailTask.setStartTime(System.currentTimeMillis());
        }
        MailTask taskToAdd = mailTaskService.save(mail.getMailTask());
        mail.setMailTask(taskToAdd);
        return repository.save(mail);
    }

    @Transactional
    public Mail changeMailStatus(Mail mail, Mail.STATUS status){
        MailLog mailLog = new MailLog();
        mailLog.setMailStatus(status);
        MailLog savedLog = mailLogService.save(mailLog);
        mail.getMailLog().add(savedLog);

        mail.setStatus(status);
        return repository.save(mail);
    }

    @Transactional
    public Mail changeMailStatus(Mail mail, Mail.STATUS status, String message){
        MailLog mailLog = new MailLog();
        mailLog.setMailStatus(status);
        mail.setMessage(message.length() > 255 ? message.substring(0, 255) : message);
        MailLog savedLog = mailLogService.save(mailLog);
        mail.getMailLog().add(savedLog);

        mail.setStatus(status);
        return repository.save(mail);
    }

    @Transactional
    public List<Mail> findBySubjectLike(String subject) {
        return repository.findBySubjectIgnoreCaseLike(subject);
    }

    @Transactional
    public Mail findBySubject(String subject){
        return repository.findBySubjectEquals(subject);
    }

    @Transactional
    public List<Mail> findByAddresseId(Long id) {
        return repository.findByAddresseeId(id);
    }

    @Transactional
    public List<Mail> findByAll(Long senderId, Integer page, Integer page_size, String sort, String direction) {
        return repository.findBySenderId(senderId, createPageable(page, page_size, sort, direction));
    }
}
