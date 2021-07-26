package com.biskot.domain.service;

import com.biskot.domain.model.Cart;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
    @Override
    public long createCart() {
        return 1L;
    }

    @Override
    public Cart getCart(long cartId) {
        // TODO: to be implemented
        return null;
    }

    @Override
    public void addItemToCart(long cartId, long productId, int quantityToAdd) {
        // TODO: to be implemented
    }
}
