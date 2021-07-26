package com.biskot.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class Product {
    private final long id;
    private final String label;
    private final BigDecimal price;
}
