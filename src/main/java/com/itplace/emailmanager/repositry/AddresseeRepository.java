package com.itplace.emailmanager.repositry;

import com.itplace.emailmanager.domain.Addressee;
import org.springframework.stereotype.Repository;

@Repository
public interface AddresseeRepository extends UuidJpaRepository<Addressee> {
}
