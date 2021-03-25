package com.burakenesdemir.stockmarket.base.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public abstract class AbstractResource {
    protected Long id;

    protected String createdDate;

    protected String updateDate;

    protected boolean success;

    protected String message;

    protected String resultCode;

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        return getClass() == obj.getClass() && Objects.equals(id, ((AbstractResource) obj).id);
    }
}