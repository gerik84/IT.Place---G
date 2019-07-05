package com.itplace.emailmanager.controller;

import com.itplace.emailmanager.domain.Role;
import com.itplace.emailmanager.domain.Sender;
import com.itplace.emailmanager.dto.SenderDto;
import com.itplace.emailmanager.repositry.RoleRepository;
import com.itplace.emailmanager.repositry.SenderRepository;
import com.itplace.emailmanager.security.PasswordEncoder;
import com.itplace.emailmanager.service.RoleService;
import com.itplace.emailmanager.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class WebController extends AbstractBaseController {

    @Autowired
    RoleService roleService;

    @Autowired
    SenderService senderService;


    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        List<Role> roles = roleService.findAllRole();

        model.addAttribute("roles", roles);
        model.addAttribute("sender", new SenderDto());
        return "admin";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @Autowired
    PasswordEncoder passwordEncoder;


    @RequestMapping(value="/sender", method= RequestMethod.POST)
    public String greetingSubmit(@Validated @ModelAttribute SenderDto senderDto, Model model) {

        if (checkAccess(only_admin)) {
            return "forbidden";
        }

        senderDto.setPassword(passwordEncoder.encode(senderDto.getPassword()));


        senderDto.setWhenCreated(System.currentTimeMillis());
        senderDto.setWhenUpdated(System.currentTimeMillis());
        try {
            senderService.save(senderDto, Sender.class);
            return "redirect:/admin";
        } catch (Exception ex) {
            return "error";
        }
    }
}
