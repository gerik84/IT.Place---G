package com.itplace.emailmanager.controller;

import com.itplace.emailmanager.dto.AddresseeDto;
import com.itplace.emailmanager.domain.Addressee;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
