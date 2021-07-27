package com.biskot.domain.model;

import com.biskot.infra.gateway.payload.ProductResponse;
import lombok.Value;

import java.math.BigDecimal;

@Value(staticConstructor = "of")
public class Product {
    long id;
    String label;
    BigDecimal price;

    public static Product fromResponse(ProductResponse productResponse) {
        return new Product(
                productResponse.getId(),
                productResponse.getLabel(),
                BigDecimal.valueOf(productResponse.getUnitPrice()));
    }
}
