package de.tgi.quarkus.sandbox.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PriceEntity {
    @Id
    private Integer price;

    public PriceEntity() {
    }

    public PriceEntity(Integer price) {
        this.price = price;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
