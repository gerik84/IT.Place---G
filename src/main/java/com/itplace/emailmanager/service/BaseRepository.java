package com.itplace.emailmanager.service;

import com.itplace.emailmanager.domain.Addressee;
import com.itplace.emailmanager.domain.BaseIdentifierEntity;
import com.itplace.emailmanager.repositry.LongJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

interface ServiceInterface<T extends BaseIdentifierEntity> {

    void save(T model);

    void delete(T model);

    List<T> findAll(int page, int pageSize, String sort, Sort.Direction direction);
    List<T> findAll();

    T findById(Long id);

    List<T> findByWhenCreatedDay(Long date);

    List<T> findByWhenUpdatedDay(Long date);

    List<T> findByWhenDeletedDay(Long date);
}

public abstract class BaseRepository<R extends LongJpaRepository, T extends BaseIdentifierEntity> implements ServiceInterface<T> {

    @Autowired
    protected R repository;

    @Override
    public void save(T model) {
        repository.save(model);
    }

    @Override
    public List<T> findAll() {
        return repository.findByWhenDeletedIsNull(null);
    }

    @Override
    public List<T> findAll(int page, int pageSize, String sort, Sort.Direction direction) {
        Sort sortBy = null;
        if (sort != null) {
            sortBy = Sort.by(direction, sort);
        }
        Pageable pageable = PageRequest.of(page, pageSize);
        if (sortBy != null) {
            pageable = PageRequest.of(page, pageSize, sortBy);
        }
        return repository.findByWhenDeletedIsNull(pageable);
//        return all.get().collect(Collectors.toList());
    }

    @Override
    public T findById(Long id) {
        Optional byId = repository.findById(id);
        return byId.isPresent() ? (T) byId.get() : null;
    }

    @Override
    public void delete(T model) {
        model.setWhenDeleted(System.currentTimeMillis());
        save(model);
    }

    @Override
    public List<T> findByWhenCreatedDay(Long date) {
        long from = getBeginDay(date);
        long to = getEndDayWithBegin(from);
        return repository.findByWhenCreatedBetween(from, to, null);
    }

    @Override
    public List<T> findByWhenUpdatedDay(Long date) {
        long from = getBeginDay(date);
        long to = getEndDayWithBegin(from);
        return repository.findByWhenUpdatedBetween(from, to, null);
    }

    @Override
    public List<T> findByWhenDeletedDay(Long date) {
        long from = getBeginDay(date);
        long to = getEndDayWithBegin(from);
        return repository.findByWhenDeletedBetween(from, to, null);
    }

    private long getBeginDay(Long date) {
        LocalDate localDate = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime localDateTime1 = localDate.atStartOfDay();
        return Timestamp.valueOf(localDateTime1).getTime();
    }

    private long getEndDayWithBegin(Long date) {
        return date + 24 * 3600 * 1000 - 1000;
    }


}



