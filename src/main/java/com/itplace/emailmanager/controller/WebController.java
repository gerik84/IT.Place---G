package com.itplace.emailmanager.controller;

import com.itplace.emailmanager.domain.Role;
import com.itplace.emailmanager.domain.Sender;
import com.itplace.emailmanager.service.SenderService;
import com.itplace.emailmanager.util.JavaMailSenderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class WebController {
    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Autowired
    private SenderService service;

    // TODO Владу сделать фронт
    @GetMapping("/")
    public String index() {
        // постраничный  поиск, работает во всех сервисах
        List<Sender> all = service.findAll(0, 20, null, null);
        List<Sender> all2 = service.findAll(1, 2, null, null);
        List<Sender> all3 = service.findAll(2, 20, null, null);
        List<Sender> all4 = service.findAll(0, 1, null, null);
        List<Sender> all5 = service.findAll(1, 1, null, null);
        List<Sender> all6 = service.findAll(2, 1, null, null);

        // Поиск по дате, ищит от начала и до конца дня
        service.findByWhenCreatedDay(1561462935000L);

        // поиск по роли
        List<Sender> admin = service.findByRole(Role.ROLE.USER);
        // поиск по почте
        List<Sender> byEmail = service.findByEmail("pavel.asdlevel@gmail.com");
        List<Sender> byEmail2 = service.findByEmail("%sdlevel%");
        // и прочие ...
        return "index";
    }
}
