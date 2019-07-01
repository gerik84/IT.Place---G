package com.itplace.emailmanager.controller;

import com.itplace.emailmanager.domain.Addressee;
import com.itplace.emailmanager.domain.Mail;
import com.itplace.emailmanager.domain.MailTask;
import com.itplace.emailmanager.domain.Sender;
import com.itplace.emailmanager.security.UserDetails.UserDetailsImpl;
import com.itplace.emailmanager.domain.Addressee;
import com.itplace.emailmanager.domain.Mail;
import com.itplace.emailmanager.domain.MailTask;
import com.itplace.emailmanager.domain.Sender;
import com.itplace.emailmanager.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        return mailService.findAll(pageNo, pageSize, sorted, null);
    }

    @GetMapping("/mails")
    public List<Mail> getMailsWithSenderId(@RequestParam(value = "first", required = false) Integer _first,
                                           @RequestParam(value = "max", required = false) Integer _max,
                                           @RequestParam(value = "sort", required = false) String _sort,
                                           @RequestParam(value = "direction", required = false) String _direction) {

        Long sender_id = senderService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
        return mailService.findByAll(sender_id, _first, _max, _sort, _direction);
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
    public ResponseEntity saveMail(@RequestBody Mail mail){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        mail.setSender(senderService.findByEmail(authentication.getName()));
        if (mail.getMailTask() != null) {
            mailTaskService.save(mail.getMailTask());
            MailTask mailTask = mailTaskService.getLastAdded();
            mail.setMailTask(mailTask);
        }

        Sender sender = senderService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        mail.setSender(sender);

        Mail save = mailService.save(mail);
        if (save != null && save.getId() != null) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/addressee/{id}/mails")
    public List<Mail> getAddresseeMails(@PathVariable Long id){
        return mailService.findByAddresseId(id);
    }

    @RequestMapping(value="/user", method = RequestMethod.PATCH)
    public void changePassword(@AuthenticationPrincipal UserDetailsImpl currentUser, @RequestBody String password){
        senderService.changePassword(currentUser, password);
    }
}
