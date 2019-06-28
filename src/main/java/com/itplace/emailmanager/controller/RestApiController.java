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

@RestController
@RequestMapping("/api")
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

    @RequestMapping(value = "/mail", method = RequestMethod.POST)
    public void saveMail(@RequestBody Mail mail){
        if (mail.getMailTask() != null) {
            mailTaskService.save(mail.getMailTask());
            MailTask mailTask = mailTaskService.getLastAdded();
            mail.setMailTask(mailTask);
        }
        mailService.save(mail);
    }

/*    @RequestMapping(value = "/mail", method = RequestMethod.POST)
    public void saveMail(@RequestBody MailWrapper mailWrapper){
        saveMessage(mailWrapper, null);
    }

    @RequestMapping(value = "/mail/send/later/{startTime}", method = RequestMethod.POST)
    public void sendMailOnce(@RequestBody MailWrapper mailWrapper, @PathVariable Long startTime){
        MailTask mailTask = new MailTask();
        mailTask.setStartTime(startTime);
        mailTask.setIntervalTime(0);
        mailTask.setRepeatsLeft(1);

        mailTaskService.save(mailTask);
        MailTask savedTask = mailTaskService.getLastAdded();
        saveMessage(mailWrapper, savedTask);
    }

    @RequestMapping(value = "/mail/send/later/{startTime}/{interval}", method = RequestMethod.POST)
    public void sendMailNow(@RequestBody MailWrapper mailWrapper, @PathVariable Long startTime, @PathVariable Long interval){
        MailTask mailTask = new MailTask();
        mailTask.setStartTime(startTime);
        mailTask.setIntervalTime(interval);
        mailTask.setRepeatsLeft(Integer.MAX_VALUE);

        mailTaskService.save(mailTask);
        MailTask savedTask = mailTaskService.getLastAdded();
        saveMessage(mailWrapper, savedTask);
    }

    @RequestMapping(value = "/mail/send/later/{startTime}/{interval}/{repeats}", method = RequestMethod.POST)
    public void sendMailNow(@RequestBody MailWrapper mailWrapper, @PathVariable Long startTime, @PathVariable Long interval, @PathVariable Integer repeats){
        MailTask mailTask = new MailTask();
        mailTask.setStartTime(startTime);
        mailTask.setIntervalTime(interval);
        mailTask.setRepeatsLeft(repeats);

        mailTaskService.save(mailTask);
        MailTask savedTask = mailTaskService.getLastAdded();
        saveMessage(mailWrapper, savedTask);
    }

    private void saveMessage(MailWrapper mailWrapper, MailTask mailTask){
        Mail mail;
        List<Long> iDs = new ArrayList<>();
        mailWrapper.getMessageAddressees().forEach(i -> iDs.add(Long.valueOf(i)));
        if (iDs.size() > 0) {
            List<Addressee> addresseeList = new ArrayList<>();
            for (Long l : iDs) {
                Addressee addressee = addresseeService.findById(l);
                if (addressee != null) addresseeList.add(addressee);
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            Sender sender = senderService.findByEmail(authentication.getName());
            Sender sender = senderService.findById(1L);

            mail = new Mail();
            mail.setAddressee(addresseeList);
            mail.setMessage(mailWrapper.getMessageText());
            mail.setSubject(mailWrapper.getMessageSubject());
            mail.setSender(sender);
            if (mailTask != null) {
                mail.setMailTask(mailTask);
                mailTask.setMail(mail);
            }
            mailService.save(mail);
        }
    }*/
}
