package com.itplace.emailmanager.service;

import com.itplace.emailmanager.domain.Mail;
import com.itplace.emailmanager.repositry.MailRepository;
import org.springframework.stereotype.Service;

@Service
public class MailService extends BaseRepository<MailRepository, Mail> {

}
