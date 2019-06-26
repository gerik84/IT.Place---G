package com.itplace.emailmanager.controller;

import com.itplace.emailmanager.domain.*;
import com.itplace.emailmanager.service.*;
import com.itplace.emailmanager.util.JavaMailSenderImpl;
import com.itplace.emailmanager.util.MessageWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    public void saveMail(@RequestBody MessageWrapper messageWrapper){
        saveMessage(messageWrapper);
    }

    @RequestMapping(value = "/mail/send/now", method = RequestMethod.POST)
    public void sendMailNow(@RequestBody MessageWrapper messageWrapper){
        Mail mail = saveMessage(messageWrapper);
        if (mail != null) javaMailSender.sendMail(mail);
    }

    @RequestMapping(value = "/mail/send/later/{timeDate}", method = RequestMethod.POST)
    public void sendMailNow(@RequestBody MessageWrapper messageWrapper, @PathVariable String timeDate){ // TODO в каком формате приходит время?
        Mail mail = saveMessage(messageWrapper);
        if (mail != null) javaMailSender.sendScheduledMail(mail, timeDate);
    }

    private Mail saveMessage(MessageWrapper messageWrapper){
        Mail mail = null;
        List<Long> iDs = new ArrayList<>();
        messageWrapper.getIds().forEach(i -> iDs.add(Long.valueOf(i)));
        if (iDs.size() > 0) {
            List<Addressee> addresseeList = new ArrayList<>();
            for (Long l : iDs) {
                Addressee addressee = addresseeService.findById(l);
                if (addressee != null) addresseeList.add(addressee);
            }
            LocalizedString localizedString = new LocalizedString();  // TODO а LocalizedString не лишнее?
            localizedString.setLocale("ru");                          // TODO может будем отправлять простую строку?
            localizedString.setValue(messageWrapper.getMessageText());
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
            mail.setSubject(messageWrapper.getMessageSubject());
            mail.setSender(sender);
            mailService.save(mail);
        }
            return mail;
    }

}
