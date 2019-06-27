package com.itplace.emailmanager.service;

import com.itplace.emailmanager.domain.Mail;
import com.itplace.emailmanager.domain.MailTask;
import com.itplace.emailmanager.repositry.MailTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailTaskService extends BaseRepository<MailTaskRepository, MailTask>{
    @Autowired
    private MailService mailService;

    public MailTask findByMailId(Long id) {
        return repository.findByMail_Id(id);
    }

    public List<MailTask> findTasks(){
        return repository.findByStatusEqualsAndStartTimeBefore(MailTask.STATUS.NEW, System.currentTimeMillis());
    }

    public void scheduleMail(Mail mail, MailTask mailTask) {
        mailTask.setMail(mail);
        repository.save(mailTask);
        mail.setMailTask(mailTask);

        mailService.save(mail);
    }
}
