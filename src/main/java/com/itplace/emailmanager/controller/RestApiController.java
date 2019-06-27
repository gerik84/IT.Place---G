package com.itplace.emailmanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.itplace.emailmanager.domain.*;
import com.itplace.emailmanager.service.*;
import com.itplace.emailmanager.util.MailWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class RestApiController {
    @Autowired
    AddresseeService addresseeService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    MailTaskService mailTaskService;
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


    @RequestMapping(value = "/departments", method = RequestMethod.GET)
    public ResponseEntity getDeportment() {
        return createResponse(departmentService.findAll());
    }

    @RequestMapping(value = "/department/{id}/addressees", method = RequestMethod.GET)
    public ResponseEntity getAddressee(@PathVariable("id") Long department) {
        return createResponse(addresseeService.findByDepartmentId(department));
    }

    private ResponseEntity createResponse(Object body) {
        return body == null ?  new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK) :
                new ResponseEntity<>(body, HttpStatus.OK);
    }

    @RequestMapping(value = "/mail/save", method = RequestMethod.POST)
    public void saveMail(@RequestBody MailWrapper mailWrapper){
        saveMessage(mailWrapper);
    }

    @RequestMapping(value = "/mail/send/later/{startTime}/{interval}/{repeats}", method = RequestMethod.POST)
    public void sendMailNow(@RequestBody MailWrapper mailWrapper, @PathVariable Long startTime, @PathVariable Long interval, @PathVariable Integer repeats){
        Mail mail = saveMessage(mailWrapper);
        if (startTime != null) {
            MailTask mailTask = new MailTask();
            mailTask.setStartTime(startTime);
            if (interval != null && repeats != null) {
                mailTask.setIntervalTime(interval);
                mailTask.setRepeatsLeft(repeats);
            }
            mailTaskService.scheduleMail(mail, mailTask);
        }
    }

    private Mail saveMessage(MailWrapper mailWrapper){
        Mail mail = null;
        List<Long> iDs = new ArrayList<>();
        mailWrapper.getMessageAddressees().forEach(i -> iDs.add(Long.valueOf(i)));
        if (iDs.size() > 0) {
            List<Addressee> addresseeList = new ArrayList<>();
            for (Long l : iDs) {
                Addressee addressee = addresseeService.findById(l);
                if (addressee != null) addresseeList.add(addressee);
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Sender sender = senderService.findByEmail(authentication.getName());

            mail = new Mail();
            mail.setAddressee(addresseeList);
            mail.setMessage(mailWrapper.getMessageText());
            mail.setSubject(mailWrapper.getMessageSubject());
            mail.setSender(sender);
            mailService.save(mail);
        }
        return mail;
    }
}
