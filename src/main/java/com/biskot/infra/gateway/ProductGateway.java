package com.biskot.infra.gateway;

import com.biskot.domain.model.Product;
import com.biskot.domain.spi.ProductPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductGateway implements ProductPort {

    public Optional<Product> getProduct(long productId) {
        // TODO: to be implemented
        return null;
    }

}
