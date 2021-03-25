package com.burakenesdemir.stockmarket.base.data.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.style.ToStringCreator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@MappedSuperclass
@Getter
@Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class IdEntity implements Serializable {
    private static final int ID_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stock_market_seq")
    @SequenceGenerator(name = "stock_market_seq", sequenceName = "stock_market_seq",allocationSize = 1)
    @Column(name = "id", length = ID_LENGTH)
    public Long id;

    public IdEntity() {
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
        return obj != null && getClass() == obj.getClass() && Objects.equals(id, ((IdEntity) obj).id);
    }

    @Override
    public String toString() {
        return toString(new ToStringCreator(this)).toString();
    }

    protected ToStringCreator toString(ToStringCreator creator) {
        return creator.append("id", getId());
    }
}