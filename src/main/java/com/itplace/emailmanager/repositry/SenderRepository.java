package com.itplace.emailmanager.repositry;

import com.itplace.emailmanager.domain.Sender;
import org.springframework.stereotype.Repository;

@Repository
public interface SenderRepository extends UuidJpaRepository<Sender> {
}
