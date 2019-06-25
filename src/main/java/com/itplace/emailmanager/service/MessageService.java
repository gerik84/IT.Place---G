package com.itplace.emailmanager.service;

import com.itplace.emailmanager.domain.Message;
import com.itplace.emailmanager.repositry.MessageRepository;
import org.springframework.stereotype.Service;

@Service
public class MessageService extends BaseRepository<MessageRepository, Message> {

}
