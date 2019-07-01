package com.itplace.emailmanager.repositry;

import com.itplace.emailmanager.domain.Mail;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MailRepository extends LongJpaRepository<Mail> {

    List<Mail> findBySubjectIgnoreCaseLike(String subject);

    List<Mail> findByStatusEqualsAndWhenCreatedBeforeAndMailTaskIsNull(Mail.STATUS status, Long timeDate);

    List<Mail> findByStatusEquals(Mail.STATUS status);

    Mail findBySubjectEquals(String string);

    List<Mail> findByAddresseeId(Long addresseeId);

    List<Mail> findBySenderId(Long senderId, Pageable pageable);
}
