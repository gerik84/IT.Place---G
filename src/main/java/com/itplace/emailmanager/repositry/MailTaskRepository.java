package com.itplace.emailmanager.repositry;

import com.itplace.emailmanager.domain.MailTask;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MailTaskRepository extends LongJpaRepository<MailTask> {
    MailTask findByMail_Id(Long mailId);

    List<MailTask> findByStatusEqualsAndStartTimeBefore(MailTask.STATUS status, Long timeDate);

    MailTask findFirstByOrderByIdDesc();
}
