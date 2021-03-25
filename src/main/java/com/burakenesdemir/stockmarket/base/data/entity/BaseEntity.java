package com.burakenesdemir.stockmarket.base.data.entity;

import com.burakenesdemir.stockmarket.base.data.entity.listener.AuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.style.ToStringCreator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;
import java.util.Objects;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity extends IdEntity {
    @CreatedDate
    @Column(name = "createdate", nullable = false)
    public Date createdDate = new Date();

    @LastModifiedDate
    @Column(name = "updatedate")
    @JsonIgnore
    public Date updateDate = new Date();

    public BaseEntity() {
    }

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
        return getClass() == obj.getClass() && Objects.equals(id, ((BaseEntity) obj).id);
    }

    @Override
    public String toString() {
        return toString(new ToStringCreator(this)).toString();
    }
}