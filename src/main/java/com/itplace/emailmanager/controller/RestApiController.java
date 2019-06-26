package com.itplace.emailmanager.controller;

import com.itplace.emailmanager.domain.Addressee;
import com.itplace.emailmanager.service.AddresseeService;
import com.itplace.emailmanager.util.JavaMailSenderImpl;
import com.itplace.emailmanager.util.MessageWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class RestApiController {
    @Autowired
    JavaMailSenderImpl javaMailSender;

    @Autowired
    AddresseeService addresseeService;

    @RequestMapping(value = "/message/sendnow", method = RequestMethod.PUT)
    public void sendNow(@RequestBody MessageWrapper messageWrapper){
        for (String s: messageWrapper.getIds()) {
            System.out.println(s);
        }
        System.out.println(messageWrapper.getMessageSubject());
        System.out.println(messageWrapper.getMessageText());
/*        List<String> idList = (List<String>) jsonObject.get("messageAddressees");
        List<Addressee> addresseeList = new ArrayList<>();
        for (String s: idList) {
            Addressee addressee = addresseeService.findById(Long.valueOf(s));
            if (addressee != null) addresseeList.add(addressee);
        }
        if (addresseeList.size() > 0) {
            ArrayList<String> emails = new ArrayList<>();
            addresseeList.forEach(a -> emails.add(a.getEmail()));
            javaMailSender.sendSimpleMessage(emails.toArray(new String[0]), (String) jsonObject.get("messageSubject"), (String) jsonObject.get("messageText"));
        }*/
    }

    @RequestMapping(value = "/message/save", method = RequestMethod.PUT)
    public void save(@RequestBody JSONObject jsonObject){

    }
}
