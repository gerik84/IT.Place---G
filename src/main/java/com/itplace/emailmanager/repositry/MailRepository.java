package com.itplace.emailmanager.repositry;

import com.itplace.emailmanager.domain.Addressee;
import com.itplace.emailmanager.domain.Mail;
import com.itplace.emailmanager.domain.MailTask;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MailRepository extends LongJpaRepository<Mail> {

    List<Mail> findBySubjectIgnoreCaseLike(String subject);

    List<Mail> findByStatusEquals(Mail.STATUS status);

    Mail findBySubjectEquals(String string);

    List<Mail> findByAddresseeId(Long addresseeId);

    List<Mail> findByWhenDeletedNullAndMailTask_StartTimeIsLessThanAndMailTask_StatusNotAndStatusNot
            (Long currentTime, MailTask.STATUS taskStatusDone, Mail.STATUS mailStatusSending);
}
