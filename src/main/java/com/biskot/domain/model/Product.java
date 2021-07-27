package com.biskot.domain.model;

import com.biskot.infra.gateway.payload.ProductResponse;
import lombok.Value;

import java.math.BigDecimal;

@Value(staticConstructor = "of")
public class Product {
    long id;
    String label;
    BigDecimal price;
    int quantityInStock;

    public static Product fromResponse(ProductResponse productResponse) {
        return Product.of(
                productResponse.getId(),
                productResponse.getLabel(),
                BigDecimal.valueOf(productResponse.getUnitPrice()),
                productResponse.getQuantityInStock()
        );
    }

    public static Product of(long id, String label, BigDecimal unitPrice) {
        return of(id, label, unitPrice, Integer.MAX_VALUE);
    }
}
