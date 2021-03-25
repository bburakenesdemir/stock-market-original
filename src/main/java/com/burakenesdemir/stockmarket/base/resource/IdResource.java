package com.burakenesdemir.stockmarket.base.resource;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdResource {
    protected String identifier;

    public IdResource() {
    }

    public IdResource(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "IdResource{" +
                "identifier='" + identifier + '\'' +
                '}';
    }
}