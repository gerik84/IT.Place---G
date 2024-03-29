package com.itplace.emailmanager.controller;

import com.itplace.emailmanager.domain.Sender;
import com.itplace.emailmanager.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SenderRestController extends RestApiController {

    @Autowired
    private SenderService senderService;

    @GetMapping("/senders")
    public ResponseEntity getSenders(){
        return createResponse(senderService.findAll());
    }

    @RequestMapping(value = "/sender", method = RequestMethod.POST)
    public ResponseEntity addSender(@RequestBody Sender sender) {

        if (checkAccess(only_admin)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Sender created = senderService.save(sender);
        return new ResponseEntity<>(created != null ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }
}
