package com.itplace.emailmanager.controller;

import com.itplace.emailmanager.domain.Addressee;
import com.itplace.emailmanager.domain.Mail;
import com.itplace.emailmanager.domain.Sender;
import com.itplace.emailmanager.security.UserDetails.UserDetailsImpl;
import com.itplace.emailmanager.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping(value = "/mail", method = RequestMethod.POST)
    public ResponseEntity saveMail(@RequestBody Mail mail){
        mail.setSender(senderService.findByEmail
                (SecurityContextHolder.getContext().getAuthentication().getName()));
        Mail created = mailService.saveNewMail(mail);

        return new ResponseEntity<>(created != null ? HttpStatus.CREATED : HttpStatus.CONFLICT);
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

    @RequestMapping(value = "/addressee", method = RequestMethod.POST)
    public ResponseEntity addAddressee(@RequestBody Addressee addressee){
        Addressee created = addresseeService.saveNewAddressee(addressee);

        return new ResponseEntity<>(created != null ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "/sender", method = RequestMethod.POST)
    public ResponseEntity addSender(@RequestBody Sender sender){
        Sender created = senderService.saveNewSender(sender);

        return new ResponseEntity<>(created != null ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "/departments", method = RequestMethod.GET)
    public ResponseEntity getDepartment() {
        return createResponse(departmentService.findAll());
    }

    @RequestMapping(value = "/department/{id}/addressees", method = RequestMethod.GET)
    public ResponseEntity getAddressee(@PathVariable("id") Long department) {
        return createResponse(addresseeService.findByDepartmentId(department));
    }

    @RequestMapping(value="/user", method = RequestMethod.PATCH)
    public void changePassword(@AuthenticationPrincipal UserDetailsImpl currentUser, @RequestBody String password){
        senderService.changePassword(currentUser, password);
    }

    private ResponseEntity createResponse(Object body) {
        return body == null ?  new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK) :
                new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("/addressee/{id}/mails")
    public List<Mail> getAddresseeMails(@PathVariable Long id){
        return mailService.findByAddresseId(id);
    }

}
