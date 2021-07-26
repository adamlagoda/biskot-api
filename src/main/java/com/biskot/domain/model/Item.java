package com.biskot.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Item {
    private final Product product;

    @Setter
    private int quantity;

    public static Item of(Product product) {
        return new Item(product, 1);
    }

    public BigDecimal getLinePrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
