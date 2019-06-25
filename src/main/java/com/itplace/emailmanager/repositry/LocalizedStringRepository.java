package com.itplace.emailmanager.repositry;

import com.itplace.emailmanager.domain.LocalizedString;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalizedStringRepository extends UuidJpaRepository<LocalizedString> {
}
