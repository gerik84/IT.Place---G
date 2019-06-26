package com.itplace.emailmanager.controller;

import com.itplace.emailmanager.domain.Addressee;
import com.itplace.emailmanager.domain.Department;
import com.itplace.emailmanager.domain.Role;
import com.itplace.emailmanager.domain.Sender;
import com.itplace.emailmanager.service.AddresseeService;
import com.itplace.emailmanager.service.SenderService;
import com.itplace.emailmanager.util.JavaMailSenderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WebController {
    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Autowired
    private SenderService service;

    @Autowired
    private AddresseeService addresseeService;

    @GetMapping("/")
    public String index(Model model) {
        List<Addressee> addresseeList = addresseeService.findAll();
        model.addAttribute("addresseeList", addresseeList);
        return "index";
    }
}
