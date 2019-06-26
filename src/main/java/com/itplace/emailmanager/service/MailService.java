package com.itplace.emailmanager.service;

import com.itplace.emailmanager.domain.Mail;
import com.itplace.emailmanager.repositry.MailRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailService extends BaseRepository<MailRepository, Mail> {

    public List<Mail> findBySubjectLike(String subject) {
        return repository.findBySubjectIgnoreCaseLike(subject);
    }
}
