package com.itplace.emailmanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.itplace.emailmanager.domain.*;
import com.itplace.emailmanager.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity saveMail(@RequestBody Mail mail){
        if (mail.getMailTask() != null) {
            mailTaskService.save(mail.getMailTask());
            MailTask mailTask = mailTaskService.getLastAdded();
            mail.setMailTask(mailTask);
        }
        mailService.save(mail);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/addressee/{id}/mails")
    public List<Mail> getAddresseeMails(@PathVariable Long id){
        return mailService.findByAddresseId(id);
    }
}
