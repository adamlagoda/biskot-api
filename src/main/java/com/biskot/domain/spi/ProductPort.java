package com.biskot.domain.spi;

import com.biskot.domain.model.Product;

import java.util.Optional;

public interface ProductPort {

    Optional<Product> getProduct(long productId);

}
