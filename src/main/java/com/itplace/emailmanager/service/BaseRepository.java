package com.itplace.emailmanager.service;

import com.itplace.emailmanager.domain.BaseUuidEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface ServiceInterface<T extends BaseUuidEntity> {

    void save(T model);
    List<T> findAll();

}

public abstract class BaseRepository<R extends JpaRepository,  T extends BaseUuidEntity> implements ServiceInterface<T> {

    @Autowired
    protected R repository;

    @Override
    public void save(T model) {
        repository.save(model);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }
}



