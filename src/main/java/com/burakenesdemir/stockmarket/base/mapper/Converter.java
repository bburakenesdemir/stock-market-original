package com.burakenesdemir.stockmarket.base.mapper;

import com.burakenesdemir.stockmarket.base.dto.IdDto;
import com.burakenesdemir.stockmarket.base.resource.IdResource;

import java.util.List;

public interface Converter <DTO, Entity, Resource>{
    Resource toResource(Entity entity);

    List<Resource> toResource(List<Entity> entityList);

    Entity toEntity(DTO dto);

    IdResource toIdResource(Entity entity);

    Entity toEntity(IdDto idDto);
}