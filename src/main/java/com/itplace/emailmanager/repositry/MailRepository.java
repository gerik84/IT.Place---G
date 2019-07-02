package com.itplace.emailmanager.repositry;

import com.itplace.emailmanager.domain.Mail;
import org.springframework.data.domain.Pageable;
import com.itplace.emailmanager.domain.MailTask;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MailRepository extends LongJpaRepository<Mail> {

    List<Mail> findBySubjectIgnoreCaseLike(String subject);

    List<Mail> findByStatusEquals(Mail.STATUS status);

    Mail findBySubjectEquals(String string);

    List<Mail> findByAddresseeId(Long addresseeId);

    List<Mail> findBySenderId(Long senderId, Pageable pageable);
  
    List<Mail> findBySenderConnectionOkTrueAndWhenDeletedNullAndMailTask_StartTimeIsLessThanAndMailTask_StatusNotAndMailTask_StatusNotAndStatusNot
            (Long currentTime, MailTask.STATUS taskStatusDone, MailTask.STATUS taskStatusCancelled, Mail.STATUS mailStatusSending);
}
