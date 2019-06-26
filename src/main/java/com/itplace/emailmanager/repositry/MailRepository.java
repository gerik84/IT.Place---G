package com.itplace.emailmanager.repositry;

import com.itplace.emailmanager.domain.Mail;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MailRepository extends LongJpaRepository<Mail> {

    List<Mail> findBySubjectIgnoreCaseLike(String subject);

}
