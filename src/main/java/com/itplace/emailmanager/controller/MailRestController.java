package com.itplace.emailmanager.controller;

import com.itplace.emailmanager.domain.Mail;
import com.itplace.emailmanager.domain.MailTask;
import com.itplace.emailmanager.service.*;
import com.itplace.emailmanager.util.AddresseeImportExport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class MailRestController extends RestApiController {

    @Autowired
    private MailService mailService;
    @Autowired
    private SenderService senderService;

    @RequestMapping(value = "/mail/{id}", method = RequestMethod.GET)
    public Mail getMail(@PathVariable("id") Long id) {
        return mailService.findById(id);
    }

    @RequestMapping(value="/mail/{id}", method = RequestMethod.PATCH)
    public ResponseEntity changeMail(@PathVariable("id") Long id, @RequestBody Mail mailIn) {
        Mail mail = mailService.findById(id);
        if (mail == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Mail save = mailService.changeMailStatus(mail, mailIn.getStatus());
        return new ResponseEntity<>(save != null ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT);
    }

    @RequestMapping(value="/mail/{id}/task/status/{status}", method = RequestMethod.PATCH)
    public ResponseEntity changeTaskStatus(@PathVariable("id") Long id, @PathVariable("status") MailTask.STATUS status) {
        Mail mail = mailService.findById(id);
        if (mail == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            mail.getMailTask().setStatus(status);
            mailService.save(mail);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/mail", method = RequestMethod.POST)
    public ResponseEntity saveMail(@RequestBody Mail mail){
        mail.setSender(senderService.findByEmail
                (SecurityContextHolder.getContext().getAuthentication().getName()));
        Mail created = mailService.createNewMail(mail);

        return new ResponseEntity<>(created != null ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @GetMapping("/mails")
    public List<Mail> getMailsWithSenderId(@RequestParam(value = "first", required = false) Integer _first,
                                           @RequestParam(value = "max", required = false) Integer _max,
                                           @RequestParam(value = "sort", required = false) String _sort,
                                           @RequestParam(value = "direction", required = false) String _direction) {

        Long sender_id = senderService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
        return mailService.findByAll(sender_id, _first, _max, _sort, _direction);
    }

    @GetMapping("/mails/page/{pageNo}/{pageSize}/{sorted}")
    public List<Mail> getMailsByPageNo(@PathVariable int pageNo, @PathVariable int pageSize, @PathVariable String sorted){
        return mailService.findAll(pageNo, pageSize, sorted, null);
    }
}
