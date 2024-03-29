package com.burakenesdemir.stockmarket.data;

import com.burakenesdemir.stockmarket.base.data.entity.BaseEntity;
import com.google.api.client.util.Value;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@Table(name = "TransactionRecord")
public class TransactionRecord extends BaseEntity {

    private String hashtag;

    private Date searchTime;
    
    private float score;

    @Value("0")
    private Integer searchCount;
}
