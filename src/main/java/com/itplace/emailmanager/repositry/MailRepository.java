package com.itplace.emailmanager.repositry;

import com.itplace.emailmanager.domain.Mail;
import org.springframework.stereotype.Repository;

@Repository
public interface MailRepository extends UuidJpaRepository<Mail> {
}
