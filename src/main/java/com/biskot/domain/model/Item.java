package com.biskot.domain.model;

import com.biskot.infra.repository.entity.ItemEntity;
import lombok.*;

import java.math.BigDecimal;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Item {
    private final Product product;

    @Setter
    private int quantity;

    public BigDecimal getLinePrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public static Item of(Product product) {
        return new Item(product, 1);
    }

    public static Item fromEntity(ItemEntity itemEntity) {
        Product product = Product.of(
                itemEntity.getId(),
                itemEntity.getLabel(),
                itemEntity.getUnitPrice()
        );
        return new Item(product, itemEntity.getQuantity());
    }
}
