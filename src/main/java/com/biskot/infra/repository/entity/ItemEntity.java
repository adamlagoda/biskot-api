package com.biskot.infra.repository.entity;

import com.biskot.domain.model.Item;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class ItemEntity {
    long id;
    String label;
    BigDecimal unitPrice;
    int quantity;

    public static ItemEntity fromDomain(Item item) {
        return new ItemEntity(
                item.getProduct().getId(),
                item.getProduct().getLabel(),
                item.getProduct().getPrice(),
                item.getQuantity());
    }
}
