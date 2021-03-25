package com.burakenesdemir.stockmarket.base.data.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractIdNameCodeEntity extends AbstractIdNameEntity {
    @Column(nullable = false)
    @NotNull
    public String code;
}