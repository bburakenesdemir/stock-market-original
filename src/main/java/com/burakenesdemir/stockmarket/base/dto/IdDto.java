package com.burakenesdemir.stockmarket.base.dto;

import com.sun.istack.NotNull;

public class IdDto {
    @NotNull
    protected String identifier;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "IdDto{" +
                "identifier='" + identifier + '\'' +
                '}';
    }
}
