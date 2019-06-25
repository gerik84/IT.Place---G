package com.itplace.emailmanager.controller;

import com.itplace.emailmanager.util.JavaMailSenderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class RestApiController {
    @Autowired
    JavaMailSenderImpl javaMailSender;

    @RequestMapping(value = "/message/send", method = RequestMethod.PUT)
    public void addEmployee(@RequestBody JSONObject jsonObject){
        List<Map<String, String>> rawList = (List<Map<String, String>>) jsonObject.get("messageAddressees");
        ArrayList<String> addresseesList = new ArrayList<>();
        for (Map<String, String> map: rawList) {
            if (map != null) addresseesList.add(map.get("addressee"));

        }
        if (addresseesList.size() > 0) javaMailSender.sendSimpleMessage(addresseesList.toArray(new String[0]), (String) jsonObject.get("messageSubject"), (String) jsonObject.get("messageText"));
    }
}
