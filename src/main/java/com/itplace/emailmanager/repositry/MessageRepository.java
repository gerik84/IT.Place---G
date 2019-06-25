package com.itplace.emailmanager.repositry;

import com.itplace.emailmanager.domain.Message;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends UuidJpaRepository<Message> {
}
