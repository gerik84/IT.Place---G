package com.itplace.emailmanager.service;

import com.itplace.emailmanager.domain.BaseIdentifierEntity;
import com.itplace.emailmanager.dto.BaseDto;
import com.itplace.emailmanager.repositry.LongJpaRepository;
import com.itplace.emailmanager.security.UserDetails.UserDetailsImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

interface ServiceInterface<T extends BaseIdentifierEntity> {

    T save(T model);
        <E extends BaseDto> T save(E model, Class<T> destinationType);

    void delete(T model);

    List<T> findAll(int page, int pageSize, String sort, String direction);
    List<T> findAll();

    T findById(Long id);

    List<T> findByWhenCreatedDay(Long date);

    List<T> findByWhenUpdatedDay(Long date);

    List<T> findByWhenDeletedDay(Long date);
}

public abstract class BaseRepository<R extends LongJpaRepository, T extends BaseIdentifierEntity> implements ServiceInterface<T> {

    private static final Integer MAX = 5000;

    @Autowired
    protected R repository;

    @Override
    @Transactional
    public T save(T model) {
        return (T) repository.save(model);
    }

    @Override
    @Transactional
    public <E extends BaseDto> T save(E model, Class<T> destinationType) {
        T entity = new ModelMapper().map(model, destinationType);
        return (T) repository.save(entity);
    }

    @Override
    @Transactional
    public List<T> findAll() {
        return repository.findByWhenDeletedIsNull(null);
    }

    @Override
    @Transactional
    public List<T> findAll(int page, int pageSize, String sort, String direction) {

        return repository.findByWhenDeletedIsNull(createPageable(page, pageSize, sort, direction));
    }

    protected Pageable createPageable(Integer page, Integer pageSize, String sort, String direction) {
        if (page == null) {
            page = 0;
        }

        if (pageSize == null) {
            pageSize = MAX;
        }

        if (direction == null) {
            direction = "ASC";
        }

        Sort sortBy = null;
        if (sort != null) {
            sortBy = Sort.by(Sort.Direction.fromString(direction), sort);
        }
        Pageable pageable = PageRequest.of(page, pageSize);
        if (sortBy != null) {
            pageable = PageRequest.of(page, pageSize, sortBy);
        }
        return pageable;
    }

    @Override
    @Transactional
    public T findById(Long id) {
        Optional byId = repository.findById(id);
        return byId.isPresent() ? (T) byId.get() : null;
    }

    @Override
    @Transactional
    public void delete(T model) {
        model.setWhenDeleted(System.currentTimeMillis());
        save(model);
    }

    @Override
    @Transactional
    public List<T> findByWhenCreatedDay(Long date) {
        long from = getBeginDay(date);
        long to = getEndDayWithBegin(from);
        return repository.findByWhenCreatedBetween(from, to, null);
    }

    @Override
    @Transactional
    public List<T> findByWhenUpdatedDay(Long date) {
        long from = getBeginDay(date);
        long to = getEndDayWithBegin(from);
        return repository.findByWhenUpdatedBetween(from, to, null);
    }

    @Override
    @Transactional
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



