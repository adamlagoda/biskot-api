package com.biskot.domain.service;

import com.biskot.domain.exception.business.MaximumCartValueExceeded;
import com.biskot.domain.model.Cart;

public interface CartService {

    long createCart();

    Cart getCart(long cartId);

    void addItemToCart(long cartId, long productId, int quantityToAdd) throws MaximumCartValueExceeded;
}
