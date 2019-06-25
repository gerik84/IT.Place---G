package com.itplace.emailmanager.controller;

import com.itplace.emailmanager.util.JavaMailSenderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @Autowired
    private JavaMailSenderImpl javaMailSender;

    // TODO Владу сделать фронт
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
