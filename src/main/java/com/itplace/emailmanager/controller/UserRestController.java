package com.itplace.emailmanager.controller;

import com.itplace.emailmanager.security.UserDetails.UserDetailsImpl;
import com.itplace.emailmanager.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class UserRestController extends RestApiController {

    @Autowired
    private SenderService senderService;

    @RequestMapping(value="/user", method = RequestMethod.PATCH)
    public void changePassword(@AuthenticationPrincipal UserDetailsImpl currentUser, @RequestBody String password){
        senderService.changePassword(currentUser, password);
    }

}
