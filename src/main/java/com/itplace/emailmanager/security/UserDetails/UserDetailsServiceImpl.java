package com.itplace.emailmanager.security.UserDetails;

import com.itplace.emailmanager.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SenderService senderService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserDetailsImpl userDetails = new UserDetailsImpl(senderService.findByEmail(s));
        if (userDetails.getUsername() != null) return userDetails;
        else throw new UsernameNotFoundException("User not found");
    }
}
