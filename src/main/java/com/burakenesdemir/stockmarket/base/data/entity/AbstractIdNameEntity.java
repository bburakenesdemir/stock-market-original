package com.burakenesdemir.stockmarket.base.data.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public abstract class AbstractIdNameEntity extends BaseEntity{
    @Column(nullable = false)
    @NotNull
    public String name;
}
