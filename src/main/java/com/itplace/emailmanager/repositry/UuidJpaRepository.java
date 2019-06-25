package com.itplace.emailmanager.repositry;

import com.itplace.emailmanager.domain.BaseUuidEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UuidJpaRepository<T extends BaseUuidEntity> extends JpaRepository<T, UUID> {
}
