package com.itplace.emailmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itplace.emailmanager.domain.*;
import com.itplace.emailmanager.service.*;
import com.itplace.emailmanager.util.JavaMailSenderImpl;
import com.itplace.emailmanager.util.MailWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class RestApiController {
    @Autowired
    JavaMailSenderImpl javaMailSender;
    @Autowired
    AddresseeService addresseeService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    MessageService messageService;
    @Autowired
    MailService mailService;
    @Autowired
    SenderService senderService;
    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/addressees")
    public List<Addressee> getAddressees(){
        return addresseeService.findAll();
    }

    @GetMapping("/addressees/department/{departmentId}")
    public List<Addressee> getAddresseesByDepartment(@PathVariable Long departmentId){
        return addresseeService.findByDepartmentId(departmentId);
    }

    @GetMapping("/departments")
    public List<Department> getDepartments(){
        return departmentService.findAll();
    }

    @GetMapping("/mails/page/{pageNo}/{pageSize}/{sorted}")
    public List<Mail> getMailsByPageNo(@PathVariable int pageNo, @PathVariable int pageSize, @PathVariable String sorted){
        String sort = null;
        Sort.Direction direction = null;
        if (sorted.equals("asc") || sorted.equals("dsc")) {
            sort = "sort";
            direction = sorted.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        }
        return mailService.findAll(pageNo, pageSize, sort, direction);
    }

    @GetMapping("/mail/{mailId}")
    public Mail getMailById(@PathVariable Long mailId){
        return mailService.findById(mailId);
    }

    @RequestMapping(value = "/mail/save", method = RequestMethod.POST)
    public void saveMail(@RequestBody MailWrapper mailWrapper){
        saveMessage(mailWrapper);
    }

    @RequestMapping(value = "/mail/send/now", method = RequestMethod.POST)
    public void sendMailNow(@RequestBody MailWrapper mailWrapper){
        Mail mail = saveMessage(mailWrapper);
        if (mail != null) javaMailSender.sendMail(mail);
    }

    @RequestMapping(value = "/mail/send/later/{timeDate}", method = RequestMethod.POST)
    public void sendMailNow(@RequestBody MailWrapper mailWrapper, @PathVariable String timeDate){ // TODO в каком формате приходит время?
        Mail mail = saveMessage(mailWrapper);
        if (mail != null) javaMailSender.sendScheduledMail(mail, timeDate);
    }

    private Mail saveMessage(MailWrapper mailWrapper){
        Mail mail = null;
        List<Long> iDs = new ArrayList<>();
        mailWrapper.getIds().forEach(i -> iDs.add(Long.valueOf(i)));
        if (iDs.size() > 0) {
            List<Addressee> addresseeList = new ArrayList<>();
            for (Long l : iDs) {
                Addressee addressee = addresseeService.findById(l);
                if (addressee != null) addresseeList.add(addressee);
            }
            LocalizedString localizedString = new LocalizedString();
            localizedString.setLocale("ru");
            localizedString.setValue(mailWrapper.getMessageText());
            List<LocalizedString> localizedStrings = new ArrayList<>();
            localizedStrings.add(localizedString);

            Message message = new Message();
            message.setText(localizedStrings);
            messageService.save(message);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Sender sender = senderService.findByEmail(authentication.getName());

            mail = new Mail();
            mail.setAddressee(addresseeList);
            mail.setMessage(message);
            mail.setSubject(mailWrapper.getMessageSubject());
            mail.setSender(sender);
            mailService.save(mail);
        }
        return mail;
    }

}
