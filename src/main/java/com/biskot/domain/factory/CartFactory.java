package com.biskot.domain.factory;

import com.biskot.domain.model.Cart;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CartFactory {

    public Cart create() {
        long id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        return new Cart(id);
    }
}
