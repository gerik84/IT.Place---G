package com.itplace.emailmanager.service;

import com.itplace.emailmanager.domain.MailLog;
import com.itplace.emailmanager.repositry.MailLogRepository;
import org.springframework.stereotype.Service;

@Service
public class MailLogService extends BaseRepository<MailLogRepository, MailLog> {
}
