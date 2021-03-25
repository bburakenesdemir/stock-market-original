package com.burakenesdemir.stockmarket.base.data.entity.listener;

import com.burakenesdemir.stockmarket.base.data.entity.BaseEntity;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

@Component
public class AuditingEntityListener {
    @PrePersist
    @PreUpdate
    public void setLastModifiedDate(BaseEntity entity) {
        if (entity.getId() == null) {
            entity.setCreatedDate(new Date());
        }
        entity.setUpdateDate(new Date());
    }
}