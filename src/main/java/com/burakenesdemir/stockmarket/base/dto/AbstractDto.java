package com.burakenesdemir.stockmarket.base.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class AbstractDto implements Serializable {
    protected Long id;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        return getClass() == obj.getClass() && Objects.equals(id, ((AbstractDto) obj).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
