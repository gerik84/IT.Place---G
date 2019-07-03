package com.itplace.emailmanager.service;

import com.itplace.emailmanager.domain.MailTask;
import com.itplace.emailmanager.repositry.MailTaskRepository;
import org.springframework.stereotype.Service;

@Service
public class MailTaskService extends BaseRepository<MailTaskRepository, MailTask>{
    public MailTask findByMailId(Long id) {
        return repository.findByMail_Id(id);
    }

}
