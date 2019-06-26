package com.itplace.emailmanager.repositry;

import com.itplace.emailmanager.domain.Role;
import com.itplace.emailmanager.domain.Sender;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SenderRepository extends LongJpaRepository<Sender> {

    List<Sender> findByEmailIgnoreCaseLike(String email);

    List<Sender> findByRole(Role role);

}
