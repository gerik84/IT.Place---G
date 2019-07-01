package com.itplace.emailmanager.repositry;

import com.itplace.emailmanager.domain.MailLog;
import org.springframework.stereotype.Repository;

@Repository
public interface MailLogRepository extends LongJpaRepository<MailLog>{
}
