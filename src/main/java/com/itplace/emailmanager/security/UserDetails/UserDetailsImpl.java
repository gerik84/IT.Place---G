package com.itplace.emailmanager.security.UserDetails;

import com.itplace.emailmanager.domain.Sender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@Component
public class UserDetailsImpl implements UserDetails {
    private Sender sender;

    public UserDetailsImpl() {
    }

    public UserDetailsImpl(Sender sender) {
        this.sender = sender;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.<GrantedAuthority>singletonList(new SimpleGrantedAuthority(sender.getRole().getName().toString()));
    }

    @Override
    public String getPassword() {
        if (sender != null) return sender.getPassword();
        else return null;
    }

    @Override
    public String getUsername() {
        if (sender != null) return sender.getEmail();
        else return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
