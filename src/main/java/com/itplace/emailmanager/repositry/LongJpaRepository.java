package com.itplace.emailmanager.repositry;

import com.itplace.emailmanager.domain.BaseIdentifierEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface LongJpaRepository<T extends BaseIdentifierEntity> extends JpaRepository<T, Long> {

    List<T> findByWhenDeletedIsNull(Pageable pageable);

    List<T> findByWhenCreatedBetween(Long from, Long to, Pageable pageable);

    List<T> findByWhenUpdatedBetween(Long from, Long to, Pageable pageable);

    List<T> findByWhenDeletedBetween(Long from, Long to, Pageable pageable);
}
