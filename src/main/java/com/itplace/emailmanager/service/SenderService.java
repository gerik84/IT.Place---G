package com.itplace.emailmanager.service;

import com.itplace.emailmanager.domain.Role;
import com.itplace.emailmanager.domain.Sender;
import com.itplace.emailmanager.repositry.RoleRepository;
import com.itplace.emailmanager.repositry.SenderRepository;
import com.itplace.emailmanager.security.PasswordEncoder;
import com.itplace.emailmanager.security.UserDetails.UserDetailsImpl;
import com.itplace.emailmanager.util.SmtpConnectionTester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class SenderService extends BaseRepository<SenderRepository, Sender> {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private SmtpConnectionTester smtpConnectionTester;

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

    // TODO
    public Sender resetConnectionStatus(Sender sender){
        Sender checkedSender = smtpConnectionTester.checkConnectionStatus(sender).join();
        return repository.save(checkedSender);
    }
}
