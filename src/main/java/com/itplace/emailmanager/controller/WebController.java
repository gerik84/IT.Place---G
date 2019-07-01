package com.itplace.emailmanager.controller;

import com.itplace.emailmanager.service.AddresseeService;
import com.itplace.emailmanager.service.SenderService;
import com.itplace.emailmanager.util.AddresseeImportExport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class WebController {
    @Autowired
    SenderService senderService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
}
