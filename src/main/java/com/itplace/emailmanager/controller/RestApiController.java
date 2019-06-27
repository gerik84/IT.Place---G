package com.itplace.emailmanager.controller;

import com.itplace.emailmanager.service.AddresseeService;
import com.itplace.emailmanager.service.DepartmentService;
import com.itplace.emailmanager.util.JavaMailSenderImpl;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RestApiController {

    @Autowired
    JavaMailSenderImpl javaMailSender;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    AddresseeService addresseeService;

    @RequestMapping(value = "/message/send", method = RequestMethod.PUT)
    public void addEmployee(@RequestBody JSONObject jsonObject){
        List<Map<String, String>> rawList = (List<Map<String, String>>) jsonObject.get("messageAddressees");
        ArrayList<String> addresseesList = new ArrayList<>();
        for (Map<String, String> map: rawList) {
            if (map != null) addresseesList.add(map.get("addressee"));

        }
        if (addresseesList.size() > 0) javaMailSender.sendSimpleMessage(addresseesList.toArray(new String[0]), (String) jsonObject.get("messageSubject"), (String) jsonObject.get("messageText"));
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
}
