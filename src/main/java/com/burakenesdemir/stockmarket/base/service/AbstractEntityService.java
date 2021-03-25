package com.burakenesdemir.stockmarket.base.service;

import com.burakenesdemir.stockmarket.base.data.entity.BaseEntity;
import com.burakenesdemir.stockmarket.base.data.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class AbstractEntityService <T extends BaseEntity, PK extends Serializable> {
    public abstract BaseRepository<T, PK> getRepository();

    protected T verifySave(T entity) {
        return entity;
    }

    protected T verifyPut(T theReal, T forSave) {
        return forSave;
    }

    protected T verifyDelete(T entity) {
        return entity;
    }

    @Transactional
    public T save(T entity) {
        verifySave(entity);
        return getRepository().save(entity);
    }

    @Transactional
    public T put(PK id, T forSave) {
        T theReal = getEntity(id);
        forSave.setId(theReal.getId());
        forSave.setCreatedDate(theReal.getCreatedDate());
        verifyPut(theReal, forSave);
        return getRepository().save(forSave);
    }

    @Transactional
    public void delete(PK id) {
        T entity = getEntity(id);
        verifyDelete(entity);
        getRepository().delete(entity);
    }

    public T getEntity(PK id) {

        Optional<T> entity = getRepository().findById(id);
        if (entity.isPresent()) {
            return entity.get();
        }
        return null;
    }

    public List<T> all() {
        return getRepository().findAll();
    }

    public Page<T> pageable(Pageable pageable) {
        Page<T> page = getRepository().findAll(pageable);
        return page;
    }
}