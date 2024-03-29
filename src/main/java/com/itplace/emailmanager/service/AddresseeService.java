package com.itplace.emailmanager.service;

import com.itplace.emailmanager.domain.Addressee;
import com.itplace.emailmanager.domain.Mail;
import com.itplace.emailmanager.repositry.AddresseeRepository;
import com.itplace.emailmanager.repositry.MailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AddresseeService extends BaseRepository<AddresseeRepository, Addressee> {
    @Autowired
    MailRepository mailRepository;

    @Transactional
    public List<Addressee> findByNameOrEmail(String value) {
        return repository.findByNameIgnoreCaseLikeOrEmailIgnoreCaseLike(value, value);
    }

    public List<Addressee> findByMailId(Long id) {
        Mail mail = mailRepository.findById(id).orElse(null);
        if (mail == null) {
            return null;
        }
        return mail.getAddressee();
    }

    @Transactional
    public List<Addressee> findByNameLike(String name) {
        return repository.findByNameIgnoreCaseLike(name);
    }

    @Transactional
    public List<Addressee> findByEmailLike(String email) {
        return repository.findByEmailIgnoreCaseLike(email);
    }

    @Transactional
    public List<Addressee> findByDepartmentId(Long departmentId) {
        return repository.findByDepartmentIdAndWhenDeletedIsNull(departmentId);
    }

    @Transactional
    public boolean existsByEmailEquals(String email){
        return repository.existsByEmailEqualsIgnoreCase(email);
    }

    @Transactional
    public Addressee createNewAddressee(Addressee addressee) {
        if (!repository.existsByEmailEqualsIgnoreCase(addressee.getEmail())) return repository.save(addressee);
        else return null;
    }
}
