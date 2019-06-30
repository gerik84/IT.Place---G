package com.itplace.emailmanager.controller;

import com.itplace.emailmanager.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @Autowired
    SenderService senderService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("currentUserId", senderService.findByEmail
                (SecurityContextHolder.getContext().getAuthentication().getName()).getId());
       return "index";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("currentUserId", senderService.findByEmail
                (SecurityContextHolder.getContext().getAuthentication().getName()).getId());
        return "admin";
    }
}
