package com.itplace.emailmanager.service;

import com.itplace.emailmanager.domain.Role;
import com.itplace.emailmanager.domain.Sender;
import com.itplace.emailmanager.repositry.RoleRepository;
import com.itplace.emailmanager.repositry.SenderRepository;
import com.itplace.emailmanager.security.PasswordEncoder;
import com.itplace.emailmanager.security.UserDetails.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SenderService extends BaseRepository<SenderRepository, Sender> {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;

    public Sender findByEmail(String email) {
        return repository.findByEmailIgnoreCase(email);
    }

    public List<Sender> findByRole(Role.ROLE role) {
        Optional<Role> byId = roleRepository.findById(role);
        if (byId.isPresent()) {
            return repository.findByRole(byId.orElseGet(null));
        }
        return null;
    }

    public void changePassword(UserDetailsImpl currentUser, String password) {
        Sender sender = repository.findByEmailEquals(currentUser.getUsername());
        sender.setPassword(passwordEncoder.encode(password));
    }

    public Sender saveNewSender(Sender sender){
        if (repository.existsByEmailEqualsIgnoreCase(sender.getEmail())) return repository.save(sender);
        else return null;
    }
}
