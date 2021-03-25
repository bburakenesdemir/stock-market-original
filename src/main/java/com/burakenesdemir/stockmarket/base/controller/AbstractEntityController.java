package com.burakenesdemir.stockmarket.base.controller;

import com.burakenesdemir.stockmarket.base.data.entity.BaseEntity;
import com.burakenesdemir.stockmarket.base.dto.AbstractDto;
import com.burakenesdemir.stockmarket.base.resource.AbstractResource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

@Component
public abstract class AbstractEntityController<DTO extends AbstractDto, Entity extends BaseEntity, Resource extends AbstractResource, PK extends Serializable>  extends AbstractReadEntityController<DTO, Entity, Resource, PK> {
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") PK id) {
        getService().delete(id);
    }

    @PostMapping
    public Resource save(@RequestBody DTO dto) {
        Entity entity = getService().save(getConverter().toEntity(dto));
        Resource resource = getConverter().toResource(entity);
        return resource;
    }

    @PutMapping("/{id}")
    public Resource put(@PathVariable("id") PK id, @RequestBody DTO dto) {
        Entity forSave = getConverter().toEntity(dto);
        Entity entity = getService().put(id, forSave);
        Resource resource = getConverter().toResource(entity);
        return resource;
    }
}