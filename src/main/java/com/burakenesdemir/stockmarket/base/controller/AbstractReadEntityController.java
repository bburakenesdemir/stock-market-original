package com.burakenesdemir.stockmarket.base.controller;

import com.burakenesdemir.stockmarket.base.data.entity.BaseEntity;
import com.burakenesdemir.stockmarket.base.dto.AbstractDto;
import com.burakenesdemir.stockmarket.base.resource.AbstractResource;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractReadEntityController<Dto extends AbstractDto, Entity extends BaseEntity, Resource extends AbstractResource, PK extends Serializable>  extends AbstractController<Dto, Entity, Resource, PK>{
    @GetMapping("/all")
    public List<Resource> all() {
        List<Entity> all = getService().all();
        return toResource(all);
    }

    @GetMapping("/{id}")
    public Resource get(@PathVariable("id") PK id) {
        return getConverter().toResource(getService().getEntity(id));
    }
}
