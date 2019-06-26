package com.itplace.emailmanager.service;

import com.itplace.emailmanager.domain.Role;
import com.itplace.emailmanager.domain.Sender;
import com.itplace.emailmanager.repositry.RoleRepository;
import com.itplace.emailmanager.repositry.SenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SenderService extends BaseRepository<SenderRepository, Sender> {

    @Autowired
    RoleRepository roleRepository;

    public List<Sender> findByEmail(String email) {
        return repository.findByEmailIgnoreCaseLike(email);
    }

    public List<Sender> findByRole(Role.ROLE role) {

        Optional<Role> byId = roleRepository.findById(role);
        if (byId.isPresent()) {
            return repository.findByRole(byId.orElseGet(null));
        }
        return null;
    }

}
